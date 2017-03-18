
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


int main (int argc, char **argv){
  const InputParams* params = getParams(argc, argv);
  if(params->mode == recieveMode){
    printf("Listening\n");
    int pipedc = open(defaultPipe, O_RDONLY);
    if(pipedc < 0) handleError();

    char buffer[L2PDUSIZE * MAXPDU];
    memset(buffer, 0, L2PDUSIZE * MAXPDU);

    struct pollfd * pfds = malloc(sizeof(struct pollfd));
    pfds->fd = pipedc;
    pfds->events = POLLIN;
    poll(pfds, 1, -1);

    int readed = read(pipedc,buffer, 120);
    if(readed < 0) handleError();
    free(pfds);
    close(pipedc);
    printBytes(buffer, L2PDUSIZE * MAXPDU, L2PDUSIZE);
    printf("Read res: %d\n",readed );

    pipedc = open(defaultPipe, O_WRONLY);

    pfds = malloc(sizeof(struct pollfd));
    pfds->fd = pipedc;
    pfds->events = POLLOUT;
    poll(pfds, 1, -1);
    if(write(pipedc, buffer, 120) < 0) handleError();
    printf("Done\n");
    free(pfds);
    close(pipedc);

  }else if(params->mode == transmitMode){
    printf("Transmitting\n");
    int filedc = open(params->file, O_RDONLY);
    layer4(filedc);
  }
  

}




