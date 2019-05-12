
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <signal.h>


const int MAX_LOG_SIZE = 1000;
char *started = "Main: Started with pid %d\n";
const char *waiting = "Main: Waiting 2 seconds\n";
const char *logfile = "log";



void handleError(){
	printf("%s\n", strerror(errno) );
	exit(0);
}

void signal_handler(int signo, siginfo_t *si, void * ucontext );

//Fix writing to log file

int main(int argc, char *argv[]){
	int logdesc = open(logfile, O_WRONLY | O_TRUNC | O_CREAT, 0644);
	if(logdesc < 0) handleError();

	char *loggingString = malloc(MAX_LOG_SIZE);
	snprintf(loggingString, MAX_LOG_SIZE, started, getpid());
	write(logdesc, loggingString, MAX_LOG_SIZE);
	free(loggingString);

	struct sigaction act; 
	struct sigaction old;
	memset (&act, '\0', sizeof(act));
	act.sa_sigaction = &signal_handler;
	act.sa_flags = SA_SIGINFO;
 
	if (sigaction(SIGHUP, &act, &old) < 0) {
		handleError();
		return 1;
	}

	while(1) {
		sleep(2);
		write(logdesc,waiting, strlen(waiting));
	}

	close(logdesc);
}

void signal_handler(int signo, siginfo_t *si, void * ucontext ){
	int logdesc = open(logfile, O_WRONLY | O_APPEND, 0644);
	char *loggingString = malloc(MAX_LOG_SIZE);

	snprintf(loggingString,
		MAX_LOG_SIZE,
		"Signal number: %d, Signal code: %d, PID of sending process: %d, User id: %d\n",
		si->si_signo, si->si_code, si->si_pid, si->si_uid);
	write(logdesc, loggingString, strlen(loggingString));
	free(loggingString);
	close(logdesc);
}
