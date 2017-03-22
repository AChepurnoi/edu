#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <fcntl.h>
#include <errno.h>

extern int errno ;
const char *filename = "log";
size_t BUFFER_SIZE = 100;
const char *DIR = "/";

void handleError(){
	printf("%s\n", strerror(errno) );
	exit(0);
}

void daemonProcess(int logfile){
	char buffer[BUFFER_SIZE];

	snprintf(buffer, BUFFER_SIZE, "PID: %d, Daemon process.\n", getpid());
	write(logfile, buffer, strlen(buffer));
	setsid();
	chdir(DIR);

	pid_t parent = getppid();
	wait(&parent);
	close(logfile);

	for(i=0;i<255;i++) close(i);
	open("/dev/null");
	dup(0);
	dup(0);
	

	while(1){}//spin

}

int main(int argc, char ** argv){

	int logfile = open(filename, O_WRONLY | O_TRUNC | O_CREAT, 0644);
	pid_t forkResult = fork();
	if (forkResult == -1)handleError();

	if (forkResult == 0){
		daemonProcess(logfile);
	}else{
		char buffer[BUFFER_SIZE];
		snprintf(buffer, BUFFER_SIZE, "PID: %d, Parent process finishing.\n", getpid());
		write(logfile, buffer, strlen(buffer));
		exit(0);
	}


}
