package ru.ColdChip.GrowtopiaServer.Events;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Packet.Sender;
import ru.ColdChip.GrowtopiaServer.Packet.Unpack;
import ru.ColdChip.GrowtopiaServer.Packet.Pack;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.*;

public class ServerEvent {
	public void OnConnect(ENetPeer peer) {
		byte[] connectByte = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00};
		Sender sender = new Sender();
		sender.Send(peer, connectByte);
	}

	public void OnReceive(ENetPeer peer, ENetPacket packet) {
		byte[] data = packet.getData();
		int type = (int)data[0];
		switch(type) {
			case 2:
				Unpack unpack = new Unpack();
				String a = unpack.UnpackTextPacket(data);
				Pack pack = new Pack();
				PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "okoko"));
				System.out.println(new String(sendData.data));
				Sender sender = new Sender();
				sender.Send(peer, sendData.data);
			break;
			case 3:

			break;
			case 4:

			break;
		}
	}

	public void OnDisconnect(ENetPeer peer) {
		byte[] connectByte = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00};
		Sender sender = new Sender();
		sender.Send(peer, connectByte);
	}
}