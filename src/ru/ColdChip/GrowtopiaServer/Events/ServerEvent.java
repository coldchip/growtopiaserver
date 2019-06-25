package ru.ColdChip.GrowtopiaServer.Events;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Packet.Sender;
import ru.ColdChip.GrowtopiaServer.Packet.Unpack;
import ru.ColdChip.GrowtopiaServer.Packet.Pack;
import ru.ColdChip.GrowtopiaServer.Packet.Constants;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.*;
import ru.ColdChip.GrowtopiaServer.Utils.*;
import ru.ColdChip.GrowtopiaServer.World.*;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

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
				{	
					Unpack unpack = new Unpack();
					String textData = unpack.UnpackTextPacket(data);
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
					Unpack unpack = new Unpack();
					String textData = unpack.UnpackTextPacket(data);
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
								PacketData spawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnSpawn"), "spawn|avatar\nnetID|0\nuserID|2388\ncolrect|0|0|20|30\nposXY|938|0\nname|``CykaBlyad``\ncountry|ru\ninvis|0\nmstate|0\nsmstate|0\ntype|local\n"));
								sender.Send(peer, spawnData.data);
							}
							break;
							default:
							{

							}
							break;
						}
					}
				}
			break;
			case 4:
				{
					System.out.println("event 4");
				}
			break;
			default:
				{
					
				}
			break;
		}
	}

	public void OnDisconnect(ENetPeer peer) {
		byte[] connectByte = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00};
		Sender sender = new Sender();
		sender.Send(peer, connectByte);
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