package ru.ColdChip.GrowtopiaServer.Events;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;

import ru.ColdChip.GrowtopiaServer.ENetJava.ENetPeer;
import ru.ColdChip.GrowtopiaServer.Packet.Sender;

public class ServerEvent {
	public void OnConnect(ENetPeer peer) {
		byte[] connectByte = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00};
		Sender send = new Sender();
		send.Send(peer, connectByte);
	}
}