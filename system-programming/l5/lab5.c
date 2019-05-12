
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



/*
	What is wokring:
		Transmitting data using FIFO pipe, between -t and -r 
	What is bad:
		Magic numbers
		Code style
		Wrong bytes endian (have to flip bytes when storing values >1 byte)

*/
int main (int argc, char **argv){
  const InputParams* params = getParams(argc, argv);
  if(params->mode == recieveMode){
    recieve(params->file,params->pipe);
  }else if(params->mode == transmitMode){
    transmit(params->file, params->pipe);
  }
  

}




