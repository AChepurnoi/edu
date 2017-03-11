
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>
#include <sys/mman.h>
#include <time.h>

char *sharedmem = "/sharedmemory";

typedef struct datum{
	pid_t pid;
	time_t time;
	char text[100];
}datum;

extern int errno ;

void handleError(){
	printf("%s\n", strerror(errno) );
	exit(0);
}

int main(int argc, char *argv[]){

	int desc = shm_open(sharedmem, O_RDWR | O_CREAT, 0777);
	if(desc < 0) handleError();

	ftruncate(desc, sizeof(datum));
	datum *dat = mmap(NULL, sizeof(datum), PROT_READ | PROT_WRITE, MAP_SHARED, desc, 0); 
	if (dat == (void *) -1) {
	    handleError();
  	}

  	while(1){
  		char buffer[100];
  		scanf("%s",buffer); 
  		printf("Old Datum: pid %d, time %ld, text: %s\n",dat->pid, dat->time, dat->text);
		msync(dat, sizeof(datum), MS_SYNC);
  		strcpy(dat->text, buffer);
  		dat->pid = getpid();
  		dat->time = time(0);
  		printf("New Datum: pid %d, time %ld, text: %s\n",dat->pid, dat->time, dat->text);
  	}
	close(desc);
	shm_unlink(sharedmem);
	return 0;
}


