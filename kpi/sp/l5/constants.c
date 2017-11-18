
#define L3HEADERSIZE_NUM 40
#define L2HEADERSIZE_NUM 17
#define MSS_NUM 60
#define L2SUFFIX_NUM 3

const char *fileparam = "-f";
const char *pipeparam = "-p";
const char *recieveModeParam = "-r";
const char *transmitModeParam = "-t";
const char *empty = "null";
const char *defaultFile = "defaultFile";
const char *defaultPipe = "/tmp/defaultPipe";

const int BUFFER_MAX_SIZE = 2000;
const int recieveMode = 1;
const int transmitMode = 0;

const int MSS = MSS_NUM;
const int L3HEADERSIZE = L3HEADERSIZE_NUM;
const int L2HEADERSIZE = L2HEADERSIZE_NUM;
const int L2SUFFIX = L2SUFFIX_NUM;
const int L3PDUSIZE = L3HEADERSIZE_NUM + MSS_NUM;
const int L2PDUSIZE = L2HEADERSIZE_NUM + L3HEADERSIZE_NUM + MSS_NUM + L2SUFFIX_NUM;
const int MAXPDU = 40;

const int RESPONSESIZE = 4;
const int TTR = 16;
