
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>

#define SERV_PORT 3000
#define BUFFER_SIZE 200

const char *closeCommand = "close";

void handleError(){
  printf("Err occured: %s\n", strerror(errno));
  exit(1);
}


int socketSetup();

int main (int argc, char **argv){

  int socketdc = socketSetup();
  char buffer[BUFFER_SIZE]; 
  char inputBuffer[BUFFER_SIZE];
  memset(buffer, 0, sizeof(buffer));
  memset(inputBuffer, 0, sizeof(inputBuffer));  


  while(1){
    puts("Waiting for input...");
    gets(inputBuffer); // alternatives?

    if(send(socketdc, inputBuffer, strlen(inputBuffer) + 1, 0) < 0) handleError();
    if(recv(socketdc, buffer, BUFFER_SIZE, 0) < 0) handleError();

    puts(buffer);
    if(strcmp(closeCommand, inputBuffer) == 0){
      printf("Closing connection\n");
      break;
    }
    memset(buffer, 0, sizeof(buffer));
    memset(inputBuffer, 0, sizeof(inputBuffer));

  }
  close(socketdc);
}

int socketSetup(){
  struct sockaddr_in servaddr;
  //creation of the socket

  int socketdc = socket (PF_INET, SOCK_STREAM, 0);
  if(socketdc < 0) handleError();

  //preparation of the socket address
  servaddr.sin_family = AF_INET;
  servaddr.sin_addr.s_addr = htonl(INADDR_LOOPBACK);
  servaddr.sin_port = htons(SERV_PORT);

  int connectResult = connect (socketdc, (struct sockaddr *) &servaddr, sizeof(servaddr));
  if(connectResult < 0) handleError();

  return socketdc;
}




