package ru.ColdChip.GrowtopiaServer.Packet;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;

public class Sender {
	private static enet en = new enet();
	public void Send(ENetPeer peer, byte[] data) {
		ENetPacket packet = en.enet_packet_create(data, ENetPacketFlag.ENET_PACKET_FLAG_RELIABLE);
		en.enet_peer_send(peer, (short)0, packet);
	}
}