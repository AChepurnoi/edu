#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>
#include <sys/types.h>

extern int errno ;

void handleError(){
	printf("%s\n", strerror(errno) );
	exit(0);
}


int main(int argc, char ** argv){

	pid_t forkResult = fork();
	pid_t curr = getpid();
	if (forkResult == -1)handleError();

	if (forkResult == 0){
		printf("Hello world from child with pid %d, forkResult: %d, gid:%d, sid: %d\n", curr, forkResult,getgid(), getsid(curr));

	}else{
		printf("Hello world from root with pid %d, Child: %d, gid:%d, sid: %d\n", curr, forkResult,getgid(), getsid(curr));
		int status = 0;
		wait(&forkResult);
	}
	printf("End, pid: %d\n", curr);

}
