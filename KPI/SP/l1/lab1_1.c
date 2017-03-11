#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <fcntl.h>
#include <unistd.h>
#include <errno.h>

extern int errno ;

void handleError(){
	printf("%s\n", strerror(errno) );
	exit(0);
}

void toLowerCase(char *string, size_t size){
	for(int i = 0; i < size; i++){
		char l = string[i];
		if (l > 64 && l < 91) string[i] = l + 32;
	}
}

int main(int argc, char *argv[]){

	int BUFFER_SIZE = 500;

	if(argc != 3){
		printf("Wrong params\n");
		return -1;
	}

	int source = open(argv[1],O_RDONLY);
	if(source < 0) handleError();

	int dest = open(argv[2], O_WRONLY | O_TRUNC | O_CREAT, 0644);
	if(dest < 0) handleError();

	char *buffer = malloc(BUFFER_SIZE);
	int total = 0;

	while(1) {
		int readed = read(source, buffer, BUFFER_SIZE);
		if (readed == 0) break;
		if (readed == -1) handleError();
		toLowerCase(buffer, readed);
		int written = write(dest, buffer, readed);
		//written < readed?
		if(written == -1 )handleError();
		total = total + readed;
	}
	free(buffer);
	close(source);
	close(dest);
	printf("%d Total bytes\n",total);
}

