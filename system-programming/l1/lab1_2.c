#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <string.h>
#include <errno.h>

extern int errno ;

void handleError(){
	printf("%s\n", strerror(errno) );
	exit(0);
}


int main(int argc, char ** argv){

	if( argc != 2){
		printf("Wrong input\n");
		exit(-1);
	}

	size_t MAXSIZE = 1024;
	fd_set rfds;
	struct timeval tv;
	/* Wait up to five seconds. */
	tv.tv_sec = 5;
	tv.tv_usec = 0;

	int retval = 0;
	while(!retval){
		printf("Waiting input within five seconds\n");
		FD_ZERO(&rfds);
		FD_SET(STDIN_FILENO, &rfds);
		retval = select(STDIN_FILENO + 1, &rfds, NULL, NULL, &tv);
		
		if (retval == -1) handleError();
		else if (retval){
			char *buffer = malloc(MAXSIZE);
			int result = read(STDIN_FILENO, buffer, MAXSIZE);
			if (result == -1) handleError();
		    printf("%s: %s\n", argv[1], buffer);
		    free(buffer);
		}
	}


}
