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



void sendPDU(const char *buffer, int len, char *pipe){
  printf("Opening pipe Sending packet\n");
  int pipedc = open(pipe, O_WRONLY);
  if(pipedc < 0) handleError();
  struct pollfd *pfds = malloc(sizeof(struct pollfd));
  pfds->fd = pipedc;
  pfds->events = POLLOUT;
  printf("Poll Sending packet for pipe %s, Pipe dc: %d\n",pipe, pipedc);
  poll(pfds, 1, -1);
  printf("After poll Sending packet\n");
  write(pipedc, buffer, len);
  printf("After write Sending packet\n");
  free(pfds);
  close(pipedc);
}

void readResponse(char *buffer, char *pipe){
  printf("Opening pipe Reading packet\n");
  int pipedc = open(pipe, O_RDONLY);
  if(pipedc < 0) handleError();
  struct pollfd *pfds = malloc(sizeof(struct pollfd));
  pfds->fd = pipedc;
  pfds->events = POLLIN;
  printf("Poll Reading packet for pipe %s, Pipe dc: %d \n", pipe, pipedc);
  poll(pfds, 1, -1);
  printf("AFter Poll reading packet\n");
  if(read(pipedc,buffer,RESPONSESIZE) < 0)handleError();
  free(pfds);
  close(pipedc);
}

void layer1(char *buffer, int len, char* pipe){
  printf("Current packet sending\n");
  printBytes(buffer, len, len);
  
  printf("Sending packet\n");
  sendPDU(buffer, len, pipe);
  char response[len];
  printf("Reading packet\n");
  readResponse(response, pipe);

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

void formL2Headers(char * buffer, const int len, char Lframe){
  int fullBlocks = len / L2PDUSIZE;
  for(int i = 0, offset = 0; i < fullBlocks + 1; i++, offset = i * L2PDUSIZE){
    int pduSize = L3PDUSIZE;
    if(i == fullBlocks) pduSize = (len % L2PDUSIZE) - L2HEADERSIZE - L2SUFFIX;
    char *item = buffer + offset;
    *(item) = 0x02;

    *(item + 1) = getByte(pduSize, 2);
    *(item + 2) = getByte(pduSize, 1);
    *(item + 3) = getByte(pduSize, 0);

    *(item + 5) = i & 0xFF;
    if(i == fullBlocks) *(item + 6) = Lframe;

    *(item + L2HEADERSIZE + pduSize + 2) = 0x03;
    short crc = (short) crc16(0,item, L2HEADERSIZE + pduSize + L2SUFFIX);
    *(item + L2HEADERSIZE + pduSize + 1) = getByte(crc, 0);
    *(item + L2HEADERSIZE + pduSize + 0) = getByte(crc, 1);
  }
}

void layer2(char *buffer, int bufferSize, char *pipe, char Lframe){
  if(mkfifo(pipe,0666)) {
    handleError();
  }
  int len = shiftBytes(buffer, 0, bufferSize);
  formL2Headers(buffer,len, Lframe);
  printBytes(buffer,len, L2PDUSIZE);
  for(int i = 0, offset = 0; i < (len / L2PDUSIZE) + 1; i++, offset = L2PDUSIZE * i){
    int blockLen = (i == (len / L2PDUSIZE)) ? len - offset : L2PDUSIZE ;
    layer1(buffer + offset, blockLen, pipe);
  }
  remove(pipe);

}

void layer3(char *buffer, int bufferSize, char* pipe, char Lframe){
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
  
  layer2(sendBuffer, size, pipe, Lframe);
}

void layer4(int filedc, char* pipe){
  char *buffer = malloc(BUFFER_MAX_SIZE);
  while(1){
    int size = read(filedc, buffer, BUFFER_MAX_SIZE);
    printf("Buffer size: %d\n", size);
    char Lframe = size < BUFFER_MAX_SIZE ? 0x0F : 0x01;
    layer3(buffer, size, pipe, Lframe);
    if(size < BUFFER_MAX_SIZE) break;
  }

  free(buffer);
}

int transmit(const char* file, char* pipe){
  int filedc = open(file, O_RDONLY);
  layer4(filedc, pipe);
  return 0;
}

