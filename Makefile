JNI_COMPILER=gcc

JAVA_COMPILER=javac

SWIG_COMPILER=swig

DISPLAY_TEXT=Compiling Files

module:
	# $(SWIG_COMPILER) -java -outdir ENetJava -package ENetJava ENetJNIToolChain/enet.i
	# ^^ PLEASE DO NOT UNCOMMENT THE TOP LINE ^^ 
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/unix.c -fPIC -DHAS_SOCKLEN_T=1 -o ENetJNIToolChain/enet/unix.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/win32.c -fPIC -o ENetJNIToolChain/enet/win32.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/protocol.c -fPIC -o ENetJNIToolChain/enet/protocol.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/peer.c -fPIC -o ENetJNIToolChain/enet/peer.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/packet.c -fPIC -o ENetJNIToolChain/enet/packet.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/list.c -fPIC -o ENetJNIToolChain/enet/list.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/host.c -fPIC -o ENetJNIToolChain/enet/host.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/compress.c -fPIC -o ENetJNIToolChain/enet/compress.lo
	$(JNI_COMPILER) -c ENetJNIToolChain/enet/callbacks.c -fPIC -o ENetJNIToolChain/enet/callbacks.lo

	$(JNI_COMPILER) -c ENetJNIToolChain/enet_wrap.c -o ENetJNIToolChain/enet_wrap.o
	$(JNI_COMPILER) -shared ENetJNIToolChain/enet/*.lo ENetJNIToolChain/enet_wrap.o -o Lib/libenet.so
	rm -r ENetJNIToolChain/enet/*.lo
	$(JAVA_COMPILER) Server.java
	jar -cvfe Build/server.jar Server *

	echo "Running Build/server.jar"

	java -jar Build/server.jar
	
