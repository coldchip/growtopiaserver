/*

	@copyright: ColdChip

*/

package ru.ColdChip.GrowtopiaServer;

import ru.ColdChip.GrowtopiaServer.ENetJava.enet;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetHost;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetEvent;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetAddress;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetEventType;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetPacket;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetPacketFlag;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetBuffer;

import ru.ColdChip.GrowtopiaServer.Structs.ServerHost;
import ru.ColdChip.GrowtopiaServer.Events.ServerEvent;

import java.nio.file.Paths;

public class Server { 

	public static void main(String[] args) {
		System.out.println("\r\n\r\n\r\n\r\n\033[32m[Thread] \033[30m\033[42mGrowtopia Server Started\033[0m\r\n\r\n");
		try {
    		System.load(Paths.get("").toAbsolutePath().toString() + "/Lib/libenet.so");
    		enet en = new enet();
    		ServerHost host = new ServerHost();
    		host.event = new ENetEvent();

    		host.address = new ENetAddress();
    		host.address.setHost("0.0.0.0", 10003);

    		host.host = en.CreateHost(host.address, 3200, 2, 0, 0);
    		host.host.setCRC32();
    		host.host.enableCompression();

    		ServerEvent serverEvent = new ServerEvent();

    		while(true) {
    			try {
					if(en.Service(host.host, host.event, 10) > 0) {
						ENetEventType type = host.event.getType();
						host.peer = host.event.getPeer();
						if(type == ENetEventType.ENET_EVENT_TYPE_CONNECT) {
							serverEvent.OnConnect(host.peer);
						}
						if(type == ENetEventType.ENET_EVENT_TYPE_RECEIVE) {
							serverEvent.OnReceive(host);
						}
						if(type == ENetEventType.ENET_EVENT_TYPE_DISCONNECT) {
							serverEvent.OnDisconnect(host.peer);
						}
					}
				} catch(Exception e) {
			    	System.err.println(e.toString());
			    }
    		}
	    } catch (UnsatisfiedLinkError e) {
			System.err.println("Native code library failed to load.\n" + e);
			System.exit(1);
	    }
	}

}