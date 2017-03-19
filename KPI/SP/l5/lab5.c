
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <poll.h>
#include "constants.h"
#include "utility.h"
#include "transmitter.h"
#include "reciever.h"


int main (int argc, char **argv){
  const InputParams* params = getParams(argc, argv);
  if(params->mode == recieveMode){
    recieve(params->file,params->pipe);
  }else if(params->mode == transmitMode){
    transmit(params->file, params->pipe);
  }
  

}




