
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
    int n;
    char *mark = "Server Received:";
    while ( (n = recv(connfd, buf, MAXLINE, 0)) > 0)  {
      char * loggingString = malloc(MAX_LOG_SIZE);
      snprintf(loggingString,
        strlen(mark) + n,
        "%s %s", mark, buf);
      
      logInFile(loggingString);
      logInFile("\n");
      free(loggingString);
      
      send(connfd, buf, n, 0);
    }

    if (n < 0) {
      logInFile("Read error\n");
      exit(1);
    }
    logInFile("Client serving is finished\n");
}

int acceptConnection(int socketdc){
    struct sockaddr_in cliaddr;
    socklen_t clilen = sizeof(cliaddr);
    int connfd = accept (socketdc, (struct sockaddr *) &cliaddr, &clilen);
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

  bind (socketdc, (struct sockaddr *) &servaddr, sizeof(servaddr));
  listen(socketdc, LISTENQ);
  return socketdc;
}




