
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>

const char *fileparam = "-f";
const char *pipeparam = "-p";
const char *recieveModeParam = "-r";
const char *transmitModeParam = "-t";
const char *empty = "null";
const char *defaultFile = "defaultFile";
const char *defaultPipe = "/tmp/defaultPipe";

const int BUFFER_MAX_SIZE = 2000;
const int recieveMode = 1;
const int transmitMode = 0;

const int MSS = 60;
const int L3HEADERSIZE = 40;
const int L2HEADERSIZE = 17;
const int L2SUFFIX = 3;
const int L3PDUSIZE = L3HEADERSIZE + MSS;
const int PDUSIZE = L2HEADERSIZE + L3PDUSIZE + L2SUFFIX;
const int PDUCOUNT = 40;

typedef struct InputParams{
  const char *file;
  const char *pipe;
  int mode;
}InputParams;

void handleError(){
  printf("%s\n", strerror(errno));
  exit(1);
}

const InputParams* getParams(int, char**);
int checkMode(int,char**);
const char *getParameter(const char *, int, char **);

void printBytes(char *array, int size, int blockSize){
  for(int i = 0; i < size; i ++){
    if(i % blockSize == 0) printf("\n");
    printf("%02X",array[i]);
  }
  printf("\n");
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

void formL2Headers(char * buffer, int len){
  int fullBlocks = len / PDUSIZE;
  // int lastBlockLen = len % PDUSIZE;

  for(int i = 0, offset = 0; i < fullBlocks; i++, offset = i * PDUSIZE){
    char *item = buffer + offset;
    *(item) = 0x02;

    *(item + 1) = PDUSIZE & 0xFF0000;
    *(item + 2) = PDUSIZE & 0x00FF00;
    *(item + 3) = PDUSIZE & 0x0000FF;

    *(item + 5) = i & 0xFF;
    *(item + L2HEADERSIZE + L3PDUSIZE + 2) = 0x03;
  }
}

void layer2(char *buffer, int bufferSize){
  int len = shiftBytes(buffer, 0, bufferSize);
  formL2Headers(buffer,len);
  printBytes(buffer,len, PDUSIZE);

}

void layer3(char *buffer, int bufferSize){
  char *sendBuffer = malloc(PDUSIZE * PDUCOUNT);
  memset(sendBuffer, 0, PDUSIZE * PDUCOUNT);
  if(bufferSize > PDUSIZE * PDUCOUNT){
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


int main (int argc, char **argv){
  const InputParams* params = getParams(argc, argv);
  int filedc = open(params->file, O_RDONLY);
  layer4(filedc);


}

// ------------------------------------------------------------------Input--------------------------------------------------------------------
const char *getParameter(const char *paramName, int argc, char ** argv){
  for(int i = 0; i < argc; i++){
    if(strcmp(paramName, argv[i]) == 0){
      return argv[i+1];
    }
  }
  return empty;
}
int checkMode(int argc,char** argv){
  for(int i = 0; i < argc; i++){
    if(strcmp(recieveModeParam, argv[i]) == 0){
      return recieveMode;
    }else if(strcmp(transmitModeParam, argv[i]) == 0){
      return transmitMode;
    }
  }
  return -1;
}
const InputParams* getParams(int argc, char** argv){
  InputParams *params = malloc(sizeof(InputParams));
  const char *file = getParameter(fileparam, argc, argv);
  if(strcmp(file,empty) == 0) params->file = defaultFile;
  else params->file = file;

  const char *pipe = getParameter(pipeparam, argc, argv);
  if(strcmp(pipe,empty) == 0) params->pipe = defaultPipe;
  else params->pipe = pipe;

  int mode = checkMode(argc, argv);
  if(mode < 0) {
    printf("Wrong mode param (-r -t)\n");
    exit(-1);
  }
  params->mode = mode;
  return params;
}



