JNI_COMPILER=gcc

JAVA_COMPILER=javac

SWIG_COMPILER=swig

DISPLAY_TEXT=Compiling Files

module:
	# $(SWIG_COMPILER) -java -outdir ENet -package Enet native_enet/enet.i
	# ^^ PLEASE DO NOT UNCOMMENT THE TOP LINE ^^ 
	$(JNI_COMPILER) -c native_enet/enet/unix.c -fPIC -DHAS_SOCKLEN_T=1 -o native_enet/enet/unix.lo
	$(JNI_COMPILER) -c native_enet/enet/win32.c -fPIC -o native_enet/enet/win32.lo
	$(JNI_COMPILER) -c native_enet/enet/protocol.c -fPIC -o native_enet/enet/protocol.lo
	$(JNI_COMPILER) -c native_enet/enet/peer.c -fPIC -o native_enet/enet/peer.lo
	$(JNI_COMPILER) -c native_enet/enet/packet.c -fPIC -o native_enet/enet/packet.lo
	$(JNI_COMPILER) -c native_enet/enet/list.c -fPIC -o native_enet/enet/list.lo
	$(JNI_COMPILER) -c native_enet/enet/host.c -fPIC -o native_enet/enet/host.lo
	$(JNI_COMPILER) -c native_enet/enet/compress.c -fPIC -o native_enet/enet/compress.lo
	$(JNI_COMPILER) -c native_enet/enet/callbacks.c -fPIC -o native_enet/enet/callbacks.lo

	$(JNI_COMPILER) -c native_enet/enet_wrap.c -o native_enet/enet_wrap.o
	$(JNI_COMPILER) -shared native_enet/enet/*.lo native_enet/enet_wrap.o -o libenet.so
	rm -r native_enet/enet/*.lo
	$(JAVA_COMPILER) Server.java
	
