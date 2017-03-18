#include <stdint.h>
#include <stddef.h>

typedef struct InputParams{
  const char *file;
  const char *pipe;
  int mode;
}InputParams;

char getByte(int, int);
void handleError();
const InputParams* getParams(int, char**);
int checkMode(int,char**);
const char *getParameter(const char *, int, char **);
void printBytes(char *array, int size, int blockSize);

// https://www.lammertbies.nl/comm/info/crc-calculation.html
// Verify there ^
uint16_t crc16(uint16_t crc, const void *buf, size_t size);


