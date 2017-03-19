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

const int MSS = 60;
const int L3HEADERSIZE = 40;
const int L2HEADERSIZE = 17;
const int L2SUFFIX = 3;
const int L3PDUSIZE = L3HEADERSIZE + MSS;
const int L2PDUSIZE = L2HEADERSIZE + L3PDUSIZE + L2SUFFIX;
const int MAXPDU = 40;

const int RESPONSESIZE = 4;
