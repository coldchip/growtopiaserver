%module enet




%apply (char *STRING, size_t LENGTH) { (const void *, size_t) };
//%apply unsigned char *INOUT { enet_uint8 * };
//%apply char *STRING { const char * hostName };
//%apply (char *STRING, size_t LENGTH) { (char * hostName, size_t nameLength) };

%{
#include "enet/include/unix.h"
#include "enet/include/types.h"
#include "enet/include/protocol.h"
#include "enet/include/list.h"
#include "enet/include/callbacks.h"
#include "enet/include/enet.h"
#include "enet/include/time.h"
#include "enet/include/utility.h"
%}

%include "enet/include/unix.h"
%include "enet/include/types.h"
%include "enet/include/protocol.h"
%include "enet/include/list.h"
%include "enet/include/callbacks.h"
%include "enet/include/enet.h"
%include "enet/include/time.h"
%include "enet/include/utility.h"
