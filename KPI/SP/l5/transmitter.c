#include "transmitter.h"
#include "constants.h"
#include "utility.h"
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/stat.h>
#include <poll.h>


void layer1(char *buffer, int len){
  remove(defaultPipe);
  if(mkfifo(defaultPipe,0666)) handleError();
  int pipedc = open(defaultPipe, O_WRONLY);
  printf("Opened pipe\n");
  struct pollfd *pfds = malloc(sizeof(struct pollfd));
  pfds->fd = pipedc;
  pfds->events = POLLOUT;
  poll(pfds, 1, -1);
  write(pipedc, buffer, 120);
  free(pfds);
  close(pipedc);

  printf("Written pipe\n");
  pipedc = open(defaultPipe, O_RDONLY);
  pfds = malloc(sizeof(struct pollfd));
  pfds->fd = pipedc;
  pfds->events = POLLIN;
  poll(pfds, 1, -1);
  char tmpBuf[160];
  if(read(pipedc,tmpBuf,120) < 0)handleError();
  printf("Readed pipe\n");
  printBytes(tmpBuf,120,120);
  free(pfds);
  close(pipedc);

}

int shiftBytes(char *memory, int accOffset, int len){
  int totalFullBlocks = len / L3PDUSIZE;
  int offsetFullBlocks = accOffset/(L2HEADERSIZE + L2SUFFIX);
  int isLast = (totalFullBlocks - offsetFullBlocks == 0);
  int blockLen = L3PDUSIZE;
  int accLength = 0;
  if(!isLast) {
    int resLen = shiftBytes(memory,accOffset + L2HEADERSIZE + L2SUFFIX, len);
    accLength = accLength + resLen;
  }else blockLen = len % L3PDUSIZE;

  int originOffset = offsetFullBlocks * L3PDUSIZE;
  //move memory
  memmove(memory + originOffset + accOffset + L2HEADERSIZE, memory + originOffset, blockLen);
  //set suffix
  memset(memory + originOffset + accOffset + L2HEADERSIZE + blockLen, 0, L2SUFFIX);
  //set header
  memset(memory + originOffset + accOffset , 0, L2HEADERSIZE);
  return L2HEADERSIZE + blockLen + L2SUFFIX + accLength;
}

void formL2Headers(char * buffer, const int len){
  int fullBlocks = len / L2PDUSIZE;
  for(int i = 0, offset = 0; i < fullBlocks + 1; i++, offset = i * L2PDUSIZE){
    int pduSize = L3PDUSIZE;
    if( i == fullBlocks) pduSize = (len % L2PDUSIZE) - L2HEADERSIZE - L2SUFFIX;
    char *item = buffer + offset;
    *(item) = 0x02;

    *(item + 1) = getByte(L2PDUSIZE, 2);
    *(item + 2) = getByte(L2PDUSIZE, 1);
    *(item + 3) = getByte(L2PDUSIZE, 0);

    *(item + 5) = i & 0xFF;
    *(item + L2HEADERSIZE + pduSize + 2) = 0x03;
    short crc = (short) crc16(0,item, L2HEADERSIZE + pduSize + L2SUFFIX);
    *(item + L2HEADERSIZE + pduSize + 1) = getByte(crc, 0);
    *(item + L2HEADERSIZE + pduSize + 0) = getByte(crc, 1);
  }
}

void layer2(char *buffer, int bufferSize){
  int len = shiftBytes(buffer, 0, bufferSize);
  formL2Headers(buffer,len);
  printBytes(buffer,len, L2PDUSIZE);
  layer1(buffer, bufferSize);
}

void layer3(char *buffer, int bufferSize){
  char *sendBuffer = malloc(L2PDUSIZE * MAXPDU);
  memset(sendBuffer, 0, L2PDUSIZE * MAXPDU);
  if(bufferSize > L2PDUSIZE * MAXPDU){
    printf("Buffer size > sending buffer size \n");
    exit(-1);
  }
  int chunkCount = 0;
  for(; chunkCount < bufferSize / MSS; chunkCount ++){

    int senderOffset = chunkCount * L3PDUSIZE ;
    int bufferOffset = chunkCount * MSS;
    memcpy(sendBuffer + senderOffset + L3HEADERSIZE, buffer + bufferOffset, MSS);
  }
  int lastChunkSize = bufferSize % MSS;
  memcpy(sendBuffer + (chunkCount * L3PDUSIZE) + L3HEADERSIZE, buffer + (chunkCount * MSS), lastChunkSize);
  int size = chunkCount * L3PDUSIZE + lastChunkSize + L3HEADERSIZE;
  printBytes(sendBuffer,size,L3PDUSIZE);
  layer2(sendBuffer, size);

}

void layer4(int filedc){
  char *buffer = malloc(BUFFER_MAX_SIZE);
  int size = read(filedc, buffer, BUFFER_MAX_SIZE);
  layer3(buffer, size);
  

}
