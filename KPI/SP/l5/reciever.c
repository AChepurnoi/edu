#include "reciever.h"
#include "constants.h"
#include "utility.h"
#include <stdlib.h>
#include <stdio.h>
#include <netinet/in.h>
#include <string.h>
#include <unistd.h>
#include <fcntl.h>
#include <errno.h>
#include <sys/stat.h>
#include <poll.h>



int readPDU(char * buffer, const char* pipe){
	printf("Listening\n");
	
    int pipedc = open(pipe, O_RDONLY);
    if(pipedc < 0) handleError();

    struct pollfd *pfds = malloc(sizeof(struct pollfd));
    pfds->fd = pipedc;
    pfds->events = POLLIN;
    printf("Poll Reading packet for pipe %s, Pipe dc: %d\n", pipe, pipedc);
    poll(pfds, 1, -1);
    printf("After Poll Reading packet\n");

    int readed = read(pipedc,buffer, L2PDUSIZE);
    printf("Readed bytes: %d\n",readed);
    if(readed < 0) handleError();
    free(pfds);
    close(pipedc);
    printBytes(buffer, L2PDUSIZE, L2PDUSIZE);
    return readed;
}

int buildResponse(char *pdu, int pduLen, char* response){
	char *pduCrc = malloc(2);
	memcpy(pduCrc, pdu + pduLen - 3, 2);
	memset(pdu + pduLen - 3, 0, 2);
	short validationCrc = crc16(0, pdu, pduLen);
	char *pduValidationCrc = malloc(2);
	*(pduValidationCrc) = getByte(validationCrc,1);
	*(pduValidationCrc + 1) = getByte(validationCrc,0);
	printf("rec crc%02X%02X\n", pduCrc[0] & 0xFF, pduCrc[1] & 0xFF);
	printf("ptr crc%02X%02X\n", pduValidationCrc[0] & 0xFF, pduValidationCrc[1] & 0xFF);

	int res = memcmp(pduCrc, pduValidationCrc, 2);

	memcpy(response, pdu, 1);
	memcpy(response + 1, pdu + 5, 1);
	if(res == 0) memset(response + 2, 0x06, 1);
	else memset(response + 2, 0x15, 1);
	memcpy(response + 3, pdu + pduLen - 1, 1);

	free(pduCrc);
	free(pduValidationCrc);
	return res;
}

int sendResponse(char *response, int responseLen, const char* pipe){
    
	printf("Opening pipe\n");
    int pipedc = open(pipe, O_WRONLY);
    if(pipedc < 0) handleError();
    struct pollfd *pfds = malloc(sizeof(struct pollfd));
    pfds->fd = pipedc;
    pfds->events = POLLOUT;
    poll(pfds, 1, -1);
    printf("Sending packet\n");
    if(write(pipedc, response, responseLen) < 0) handleError();
    printf("Done\n");
    free(pfds);
    close(pipedc);
    return 0;
}

int r_layer1(char* buffer, const char *pipe){
	
	int len = readPDU(buffer, pipe);
	char *response = malloc(RESPONSESIZE);
	buildResponse(buffer, len, response);
	printBytes(response, 4, 4);
	sendResponse(response, RESPONSESIZE, pipe);
	return len;
}

void r_layer2(const char *pipe){
	char buffer[L2PDUSIZE * MAXPDU];
	int c = 0;
	while(1){
		int len = r_layer1(buffer + c, pipe);
		char Lframe = *(buffer + c + 6);
		c = c + len;
		printf("L frame %X\n", Lframe);
		if(Lframe) break;	
	}
	printBytes(buffer, c, L2PDUSIZE);

}

void recieve(const char *file, const char *pipe){
	r_layer2(pipe);
}

