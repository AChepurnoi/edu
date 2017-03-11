
#include <stdlib.h>
#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>

#define MAXLINE 50
#define SERV_PORT 3000 
#define LISTENQ 8 
#define MAX_LOG_SIZE 200

const char * logfile = "log";
const char *mark = "Server Received:";
const char *closeCommand = "close";
const char *newline = "\n";

int logd = 0;

void logInFile(const char *str){
  write(logd, str, strlen(str));
}

void handleError(){
  printf("%s\n", strerror(errno));
  logInFile(strerror(errno));
  exit(1);
}


int socketSetup();
int acceptConnection(int);
void handleConnection(int);

int main (int argc, char **argv){
  logd = open(logfile, O_WRONLY | O_TRUNC | O_CREAT, 0644);
  if(logd < 0) {
    printf("Error opening logfile\n");
    exit(1);
  }

  int socketdc = socketSetup();
  logInFile("Server running...waiting for connections.\n");

  while(1){
    int connfd = acceptConnection(socketdc);
    if(connfd < 0) handleError();

    logInFile("Received request...\n");
    pid_t forkres = fork();
    if(forkres == 0){
      handleConnection(connfd);
      close(connfd);
      exit(0);
    }
  }
  close(socketdc);
  close(logd);
}

void handleConnection(int connfd){
  char buf[MAXLINE];
  memset(buf, 0, sizeof(buf));
  int n;
  
  while ( (n = recv(connfd, buf, MAXLINE, 0)) > 0){
    char *loggingString = malloc(MAX_LOG_SIZE);
    strcpy(loggingString, mark);
    strcat(loggingString, buf);
    strcat(loggingString, newline);
    logInFile(loggingString);
    send(connfd, loggingString, strlen(loggingString), 0);
    free(loggingString);
    if(strcmp(closeCommand, buf) == 0) break;
    memset(buf, 0, sizeof(buf));
    
  }
  if (n < 0) handleError();

  logInFile("Client serving is finished\n");
}

int acceptConnection(int socketdc){
  struct sockaddr_in cliaddr;
  socklen_t clilen = sizeof(cliaddr);
  int connfd = accept (socketdc, (struct sockaddr *) &cliaddr, &clilen);
  if(connfd < 0) handleError();
  return connfd;
}

int socketSetup(){
  struct sockaddr_in servaddr;
  //creation of the socket
  int socketdc = socket (PF_INET, SOCK_STREAM, 0);
  //preparation of the socket address
  servaddr.sin_family = AF_INET;
  servaddr.sin_addr.s_addr = htonl(INADDR_ANY);
  servaddr.sin_port = htons(SERV_PORT);

  int bindRes = bind(socketdc, (struct sockaddr *) &servaddr, sizeof(servaddr));
  if(bindRes < 0) handleError();

  listen(socketdc, LISTENQ);
  return socketdc;
}




