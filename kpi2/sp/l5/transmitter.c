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
  int pipedc = open(pipe, O_WRONLY);
  if(pipedc < 0) handleError();

  // struct pollfd *pfds = createPollfd(pipedc, POLLOUT);
  // if(poll(pfds, 1, -1) < 0) handleError();
  if(write(pipedc, buffer, len) < len) handleError();

  // free(pfds);
  close(pipedc);
}

int readResponse(char *buffer, char *pipe){
  int pipedc = open(pipe, O_RDONLY);
  if(pipedc < 0) return -1;

  // struct pollfd *pfds = createPollfd(pipedc, POLLIN);
  // if(poll(pfds, 1, -1) < 0) handleError();

  int len = read(pipedc,buffer,RESPONSESIZE);
  if(len < 0) handleError();

  // free(pfds);
  close(pipedc);
  return len;
}

int validateResponse(char *response, int len){
  if(len != RESPONSESIZE) return -1;
  char res = *(response + 2);
  if(res == 0x06) return 0;
  else return -1;
  //maybe i should set errno
}

int t_layer1(char *buffer, int len, char* pipe){
  char *response = malloc(RESPONSESIZE);
  int result = -1;
  for(int i = 0; i < TTR && result < 0; i++){
    sendPDU(buffer, len, pipe);
    int responseLen = readResponse(response, pipe);
    result = validateResponse(response, responseLen);
  }
  free(response);
  return result;

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

    //Store numbers as big endian :(
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

void t_layer2(char *buffer, int bufferSize, char *pipe, char Lframe){

  int len = shiftBytes(buffer, 0, bufferSize);
  formL2Headers(buffer,len, Lframe);

  // printf("Formed buffer:");
  // printBytes(buffer, len, L2PDUSIZE);

  for(int i = 0, offset = 0; i < (len / L2PDUSIZE) + 1; i++, offset = L2PDUSIZE * i){
    int blockLen = (i == (len / L2PDUSIZE)) ? len - offset : L2PDUSIZE ;
    int res = -1;
    for(int j = 0; j < TTR && res < 0; j++){
      res = t_layer1(buffer + offset, blockLen, pipe);
    }

    if(res < 0) {
      printf("Critical error. Can't transmit packet with layer 1");
      exit(-1);
    }
  }

}

int formL3Headers(char *data, int len,  char* buffer){

  int chunkCount = 0;
  while(chunkCount < len / MSS){
    int senderOffset = chunkCount * L3PDUSIZE ;
    int bufferOffset = chunkCount * MSS;
    memcpy(buffer + senderOffset + L3HEADERSIZE, data + bufferOffset, MSS);
    chunkCount = chunkCount + 1;
  }

  int lastChunkSize = len % MSS;
  memcpy(buffer + (chunkCount * L3PDUSIZE) + L3HEADERSIZE, data + (chunkCount * MSS), lastChunkSize);
  int size = chunkCount * L3PDUSIZE + lastChunkSize + L3HEADERSIZE;
  return size;
}

void t_layer3(char *buffer, int bufferSize, char* pipe, char Lframe){  
  if(bufferSize > L2PDUSIZE * MAXPDU){
    printf("Buffer size > sending buffer size \n");
    exit(-1);
  }
  char *sendBuffer = malloc(L2PDUSIZE * MAXPDU);
  memset(sendBuffer, 0, L2PDUSIZE * MAXPDU);
  int size = formL3Headers(buffer, bufferSize, sendBuffer);
  
  t_layer2(sendBuffer, size, pipe, Lframe);
}

void t_layer4(int filedc, char* pipe){
  char *buffer = malloc(BUFFER_MAX_SIZE);
  while(1){
    int size = read(filedc, buffer, BUFFER_MAX_SIZE);
    char Lframe = size < BUFFER_MAX_SIZE ? 0x0F : 0x01;
    t_layer3(buffer, size, pipe, Lframe);
    if(size < BUFFER_MAX_SIZE) break;
  }
  free(buffer);
}

int transmit(const char* file, char* pipe){
  //check with stat
  if(mkfifo(pipe,0666)) {
    handleError();
  }
  int filedc = open(file, O_RDONLY);
  t_layer4(filedc, pipe);
  remove(pipe);
  return 0;

}

