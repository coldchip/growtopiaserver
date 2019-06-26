package ru.ColdChip.GrowtopiaServer.Events;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Packet.Sender;
import ru.ColdChip.GrowtopiaServer.Packet.Unpack;
import ru.ColdChip.GrowtopiaServer.Packet.Pack;
import ru.ColdChip.GrowtopiaServer.Packet.Constants;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.*;
import ru.ColdChip.GrowtopiaServer.Utils.*;
import ru.ColdChip.GrowtopiaServer.World.*;
import ru.ColdChip.GrowtopiaServer.Player.Structs.*;
import ru.ColdChip.GrowtopiaServer.Player.Movement.*;
import ru.ColdChip.GrowtopiaServer.Player.Movement.Structs.*;
import ru.ColdChip.GrowtopiaServer.Structs.ServerHost;

import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerEvent {

	private static HashMap<Long, PlayerData> playerData = new HashMap<Long, PlayerData>();

	private static int cid = 0;

	public void OnConnect(ENetPeer peer) {
		byte[] connectByte = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00};
		Sender sender = new Sender();
		sender.Send(peer, connectByte);

		PlayerData pData = new PlayerData();
		pData.netID = cid;
		playerData.put(peer.getPointer(), pData);
		cid++;
	}

	public void OnReceive(ServerHost host) {
		ENetPeer peer = host.peer;
		ENetPacket packet = host.event.getPacket();
		long myPointer = peer.getPointer();

		Unpack unpack = new Unpack();
		PlayerData player = playerData.get(myPointer);
		int type = unpack.getType(packet);
		switch(type) {
			case 2:
				{	
					String textData = unpack.unpackTextPacket(packet);
					Vectorize vector = new Vectorize(textData);
					if(vector.containsKey("tankIDName") && vector.containsKey("tankIDPass")) {
						if(vector.get("tankIDName").equals("a") && vector.get("tankIDName").equals("a")) {
							Pack pack = new Pack();
							PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`2Login Successful"));
							Sender sender = new Sender();
							sender.Send(peer, sendData.data);
							PacketData loginPacket = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.AppendString(pack.AppendString(pack.AppendInt(pack.AppendString(pack.CreatePacket(), "OnSuperMainStartAcceptLogonHrdxs47254722215a"), 1742202771), "ubistatic-a.akamaihd.net"), "0098/CDNContent3/cache/"), "lol.com"), "proto=42|choosemusic=audio/mp3/about_theme.mp3|active_holiday=0|"));
							sender.Send(peer, loginPacket.data);
						} else {
							Pack pack = new Pack();
							PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4Invalid Password"));
							Sender sender = new Sender();
							sender.Send(peer, sendData.data);
						}
					} else {
						if(vector.containsKey("action")) {
							switch(vector.get("action")) {
								case "refresh_item_data":
									{
										PacketData itemsDat = SaveDatPacket();
										Sender sender = new Sender();
										sender.Send(peer, itemsDat.data);
										// new enet().enet_peer_disconnect_later(peer, 0);
									}
								break;
								case "helpmenu":
									{
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnDialogRequest"), "set_default_color|`o\n\nadd_label_with_icon|big|`2Growtopia Server - Java Edition ``|left|1796|\n\nadd_spacer|small|\nadd_label_with_icon|small|`4SERVER`o:`2Growtopia Server - Java Edition`o was made by `9ColdChip`o and everyone that contributed on `9Github`o!|eft|5016|\nadd_label_with_icon|small| |left|6746|\nadd_label_with_icon|small|`4REMINDER`o: This server is under development and may be buggy please report issues on github.|left|1432|\nadd_label_with_icon|small|  |left|6746|\nadd_label_with_icon|small||\n\nadd_url_button|``GitHub: `1Here you can report any issues you have!``|`1Github Page|https://github.com/coldchip/growtopiaserver|Open link?|0|0|\nadd_url_button|||small|\nadd_textbox| |small||small|\nadd_textbox|~`2The Dev Team|small||small|\nadd_textbox|Version 0.001|small|\nadd_quick_exit|\nadd_button|chc0|Close|noflags|0|0|\nnend_dialog|gazette||OK|"));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
									}
								break;
								case "enter_game":
									{
										{
											Pack pack = new Pack();
											PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnRequestWorldSelectMenu"), "default|LOL\nadd_button|Showing: `wWorlds``|_catselect_|0.6|3529161471|\nadd_floater|LOL|0|0.55|3529161471\n"));
											Sender sender = new Sender();
											sender.Send(peer, sendData.data);
										}
										{
											Pack pack = new Pack();
											PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`2Welcome Back!"));
											Sender sender = new Sender();
											sender.Send(peer, sendData.data);
										}
										{
											Pack pack = new Pack();
											PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnDialogRequest"), "set_default_color|`o\n\nadd_label_with_icon|big|`2Growtopia Server - Java Edition ``|left|1796|\n\nadd_spacer|small|\nadd_label_with_icon|small|`4SERVER`o:`2Growtopia Server - Java Edition`o was made by `9ColdChip`o and everyone that contributed on `9Github`o!|eft|5016|\nadd_label_with_icon|small| |left|6746|\nadd_label_with_icon|small|`4REMINDER`o: This server is under development and may be buggy please report issues on github.|left|1432|\nadd_label_with_icon|small|  |left|6746|\nadd_label_with_icon|small||\n\nadd_url_button|``GitHub: `1Here you can report any issues you have!``|`1Github Page|https://github.com/coldchip/growtopiaserver|Open link?|0|0|\nadd_url_button|||small|\nadd_textbox| |small||small|\nadd_textbox|~`2The Dev Team|small||small|\nadd_textbox|Version 0.001|small|\nadd_quick_exit|\nadd_button|chc0|Close|noflags|0|0|\nnend_dialog|gazette||OK|"));
											Sender sender = new Sender();
											sender.Send(peer, sendData.data);
										}
									}
								break;
								default:
									{
										System.out.println(vector.get("action"));
									}
								break;
							}
						} else {
							Pack pack = new Pack();
							PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4" + textData));
							Sender sender = new Sender();
							sender.Send(peer, sendData.data);
						}
					}
				}
			break;
			case 3:
				{
					String textData = unpack.unpackTextPacket(packet);
					Vectorize vector = new Vectorize(textData);
					if(vector.containsKey("action")) {
						String action = vector.get("action");
						switch(action) {
							case "join_request":
							{
								GetWorldData worldUtils = new GetWorldData();
								PacketData worldData = worldUtils.GetWorld("lol");
								Sender sender = new Sender();
								sender.Send(peer, worldData.data);

								Pack pack = new Pack();
								PacketData mySpawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnSpawn"), "spawn|avatar\nnetID|" + playerData.get(myPointer).netID + "\nuserID|2388\ncolrect|0|0|20|30\nposXY|938|0\nname|``" + player.username + "``\ncountry|ru\ninvis|0\nmstate|0\nsmstate|0\ntype|local\n"));
								sender.Send(peer, mySpawnData.data);

								for (Long playerPointer : playerData.keySet()) {
									if(playerPointer != myPointer) {
										System.out.println(playerData.get(playerPointer).netID);
										PacketData spawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnSpawn"), "spawn|avatar\nnetID|" + playerData.get(playerPointer).netID + "\nuserID|2388\ncolrect|0|0|20|30\nposXY|938|0\nname|``" + playerData.get(playerPointer).username + "``\ncountry|ru\ninvis|0\nmstate|0\nsmstate|0\n"));
										sender.Send(peer, spawnData.data);
									}
								}

								for (Long playerPointer : playerData.keySet()) {
									if(playerPointer != myPointer) {
										ENetPeer playerX = new ENetPeer(playerPointer, true);
										PacketData spawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnSpawn"), "spawn|avatar\nnetID|" + playerData.get(playerPointer).netID + "\nuserID|2388\ncolrect|0|0|20|30\nposXY|938|0\nname|``" + playerData.get(playerPointer).username + "``\ncountry|ru\ninvis|0\nmstate|0\nsmstate|0\n"));
										sender.Send(playerX, spawnData.data);
									}
								}
							}
							break;
							case "quit_to_exitt":
							{
								Pack pack = new Pack();
								PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnRequestWorldSelectMenu"), "default|LOL\nadd_button|Showing: `wWorlds``|_catselect_|0.6|3529161471|\nadd_floater|LOL|0|0.55|3529161471\n"));
								Sender sender = new Sender();
								sender.Send(peer, sendData.data);
							}
							break;
							default:
							{
								System.out.println(action);
							}
							break;
						}
					}
				}
			break;
			case 4:
				{
					byte[] packetData = unpack.unpackBinary(packet);
					Movement move = new Movement();
					MovementData movementData = move.unpackMovement(packetData);

					for (Long playerPointer : playerData.keySet()) {
						if(playerPointer != myPointer) {
							System.out.println("Broadcast to: " + playerPointer);
							ENetPeer playerX = new ENetPeer(playerPointer, true);
							movementData.netID = playerData.get(playerPointer).netID;

							byte[] d = move.packMovement(movementData);
							Sender sender = new Sender();
							PacketData p = new PacketData();
							p.data = d;
							sender.Send(playerX, p.data);
						}
					}
				}
			break;
			default:
				{
					
				}
			break;
		}
	}

	public void OnDisconnect(ENetPeer peer) {
		// byte[] connectByte = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00};
		// Sender sender = new Sender();
		// sender.Send(peer, connectByte);
	}

	private PacketData SaveDatPacket() {
		try {
			File file =new File(Paths.get("").toAbsolutePath().toString() + "/Lib/items.dat");
			byte[] data = new byte[60 + (int)file.length()];
			byte[] fileData = Files.readAllBytes(file.toPath());
			byte[] headerData = new Hex2Byte().Compute(Constants.ItemsDatHeader);
			System.arraycopy(headerData, 0, data, 0, headerData.length);
			System.arraycopy(toBytes((int)file.length()), 0, data, 56, 4);
			System.arraycopy(fileData, 0, data, 60, (int)file.length());
			PacketData pd = new PacketData();
			pd.data = data;
			return pd;
		} catch(Exception e) {
			System.out.println(e.toString());
			return new PacketData();
		}
	}

	int GetHash() {
		try {
			File file =new File(Paths.get("").toAbsolutePath().toString() + "/Lib/items.dat");
			byte[] fileData = Files.readAllBytes(file.toPath());
			int acc = 0x55555555;
		    for (int i = 0; i < file.length(); i++) {
		        acc = (acc >> 27) + (acc << 5) + fileData[i];
		    }
			return acc;
		} catch(Exception e) {
			return 0;
		}
	}

	byte[] toBytes(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i);
		result[1] = (byte) (i >> 8);
		result[2] = (byte) (i >> 16);
		result[3] = (byte) (i >> 24);

		return result;
	}
}
