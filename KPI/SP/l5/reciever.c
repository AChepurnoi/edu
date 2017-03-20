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

  int pipedc = open(pipe, O_RDONLY);
  if(pipedc < 0) handleError();

  struct pollfd *pfds = createPollfd(pipedc, POLLIN);
  if(poll(pfds, 1, -1) < 0) handleError();

  int readed = read(pipedc,buffer, L2PDUSIZE);
  if(readed < 0) handleError();
  
  free(pfds);
  close(pipedc);
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

    int pipedc = open(pipe, O_WRONLY);
    if(pipedc < 0) handleError();

    struct pollfd *pfds = createPollfd(pipedc, POLLOUT);
    if(poll(pfds, 1, -1) < 0) handleError();
    if(write(pipedc, response, responseLen) < 0) handleError();

    free(pfds);
    close(pipedc);
    return 0;
}


int removeL2Headers(char * buffer, int len){
	int offset = 0;
	for(int i = 0,totalBlocks = (len / L2PDUSIZE); i < totalBlocks + 1; i++){
		char * ptr = buffer + offset;
		char * src = buffer + i * L2PDUSIZE;
		int l3Len = (i == totalBlocks) ? (len % L2PDUSIZE) - L2HEADERSIZE - L2SUFFIX : L3PDUSIZE;
		memmove(ptr, src + L2HEADERSIZE, l3Len);
		offset = offset + l3Len;
	}
	return offset;
}

int removeL3Headers(char *buffer, int len){
	int offset = 0;
	for(int i = 0, totalBlocks = (len / L3PDUSIZE); i < totalBlocks + 1; i++){
		char *ptr = buffer + offset;
		char * src = buffer + i * L3PDUSIZE;
		int dataLen = (i == totalBlocks) ? (len % L3PDUSIZE) - L3HEADERSIZE : MSS;
		memmove(ptr,src + L3HEADERSIZE, dataLen);
		offset = offset + dataLen;
	}
	return offset;
}

int r_layer1(char* buffer, const char *pipe){
	
	int len = readPDU(buffer, pipe);
	char *response = malloc(RESPONSESIZE);
	buildResponse(buffer, len, response);
	sendResponse(response, RESPONSESIZE, pipe);
	return len;
}

int r_layer2(char *buffer, char* Lframe, const char *pipe){
	int c = 0;
	while(1){
		int len = r_layer1(buffer + c, pipe);
		*(Lframe) = *(buffer + c + 6);
		c = c + len;
		if(*Lframe) break;	
	}

	// printBytes(buffer, c, L2PDUSIZE);
	int len = removeL2Headers(buffer, c);
	// printBytes(buffer, len, L3PDUSIZE);
	return len;

}


int r_layer3(char * buffer, char *Lframe, const char* pipe){
	int len = r_layer2(buffer, Lframe, pipe);
	len = removeL3Headers(buffer, len);
	// printBytes(buffer, len, MSS);
	return len;
} 

int r_layer4(const char* file, const char* pipe){
	char *buffer = malloc(L2PDUSIZE * MAXPDU);
	char *Lframe = malloc(1);

	for(int i = 0; ; i++){
		int mode = (i == 0) ? (O_WRONLY | O_TRUNC | O_CREAT) : (O_WRONLY | O_APPEND);
		int dest = open(file, mode, 0644);
		if(dest < 0) handleError();

		int len = r_layer3(buffer, Lframe, pipe);
		write(dest, buffer, len);
		
		close(dest);
		if(*Lframe == 0x0F) break;
	}
	free(Lframe);
	free(buffer);
	return 0;
}

void recieve(const char *file, const char *pipe){
	r_layer4(file, pipe);

}

