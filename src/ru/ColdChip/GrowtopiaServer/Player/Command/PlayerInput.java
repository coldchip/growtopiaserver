package ru.ColdChip.GrowtopiaServer.Player.Command;
import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Packet.Pack;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.*;
import ru.ColdChip.GrowtopiaServer.Packet.Sender;
import ru.ColdChip.GrowtopiaServer.Player.PlayerList;
import ru.ColdChip.GrowtopiaServer.Player.Structs.*;

public class PlayerInput {
	private ENetPeer peer = null;
	public PlayerInput(ENetPeer peer) {
		this.peer = peer;
	}
	public void processCommand(String message) throws Exception {
		if(peer != null) {
			if(message != null && message.length() > 0) {
				Long myPointer = this.peer.getPointer();
				PlayerList playerData = new PlayerList();
				if(message.substring(0, 1).equals("/") && message.length() > 1) {
					String command = "";
					String arg = "";
					try {
						String[] parts = message.substring(1).split("\\s+");
						command = parts[0];
						arg = parts[1];
					} catch(Exception e) {
						command = message.substring(1);
					} 
					// User imput commands. 
					switch(command) {
						case "nick":
							{
								if(arg.length() >= 5) {
									Pack pack = new Pack();
									PacketData p = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnNameChanged"), arg));
									System.arraycopy(Int2Byte(playerData.get(myPointer).netID), 0, p.data, 8, 4);
									Sender sender = new Sender();
									for (Long playerPointer : playerData.keySet()) {
										if(playerData.get(playerPointer).currentWorld == "ABC") {
											ENetPeer playerX = new ENetPeer(playerPointer, false);
											sender.Send(playerX, p.data);
										}
									}
									PlayerData update = playerData.get(myPointer);
									update.username = arg;
									playerData.put(myPointer, update);
								} else {
									Pack pack = new Pack();
									PacketData p = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4Name must be more than 5 characters"));
									Sender sender = new Sender();
									sender.Send(peer, p.data);
								}
							}
						break;
						default:
							{
								Pack pack = new Pack();
								PacketData p = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4Command not found"));
								Sender sender = new Sender();
								sender.Send(peer, p.data);
							}
						break;
					}
				} else {
					// Normal text
					Pack pack = new Pack();
					Sender sender = new Sender();
					PacketData p = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`o<`w" + playerData.get(myPointer).username + "`o> " + message));
					PacketData p2 = pack.PacketEnd(pack.AppendIntx(pack.AppendString(pack.AppendIntx(pack.AppendString(pack.CreatePacket(), "OnTalkBubble"), playerData.get(myPointer).netID), message), 0));
					for (Long playerPointer : playerData.keySet()) {
						if(playerData.get(playerPointer).currentWorld == "ABC") {
							ENetPeer playerX = new ENetPeer(playerPointer, false);
							sender.Send(playerX, p.data);
							sender.Send(playerX, p2.data);
						}
					}
				}
			}
		} else {
			throw new Exception("Command from who? ERROR_PEER_COMMAND_SENDER_NOT_FOUND");
		}
	}

	private byte[] Int2Byte(int val) {
		byte[] ret = new byte[4];
		ret[0] = (byte)(val & 0xff);
		ret[1] = (byte)((val >> 8) & 0xff);
		ret[2] = (byte)((val >> 16) & 0xff);
		ret[3] = (byte)((val >> 24) & 0xff);
		return ret;
	}
}