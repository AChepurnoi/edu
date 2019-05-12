#include "utility.h"
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include "constants.h"
#include <poll.h>

char getByte(int number, int n){
  return (char) (number >> (8 * n)) & 0xFF;
}


struct pollfd* createPollfd(int fd, short events){
  struct pollfd *pfds = malloc(sizeof(struct pollfd));
  pfds->fd = fd;
  pfds->events = events;
  return pfds;
}

void handleError(){
  printf("%s\n", strerror(errno));
  exit(1);
}

void printBytes(const char *array, int size, int blockSize){
  for(int i = 0; i < size; i ++){
    if(i % blockSize == 0) printf("\n");
    printf("%02X",array[i] & 0xFF); //clears all but 1byte
  }
  printf("\n");
}

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


//get opt?
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

