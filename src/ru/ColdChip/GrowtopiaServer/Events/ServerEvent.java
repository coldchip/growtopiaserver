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
import ru.ColdChip.GrowtopiaServer.Player.Inventory.Inventory;
import ru.ColdChip.GrowtopiaServer.Player.Command.PlayerInput;
import ru.ColdChip.GrowtopiaServer.Player.PlayerList;

import java.util.*;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;

public class ServerEvent {

	public static PlayerList playerData = new PlayerList();

	public PutWorldData putWorldData = new PutWorldData();

	private static byte[][] breaks = new byte[100][100];

	private static int cid = 0;

	public void OnConnect(ENetPeer peer) {
		byte[] connectByte = new byte[] {0x01, 0x00, 0x00, 0x00, 0x00};
		Sender sender = new Sender();
		sender.Send(peer, connectByte);

		PlayerData pData = new PlayerData();
		pData.netID = cid;
		playerData.put(peer.getPointer(), pData);
		cid += 1;
	}

	public void OnReceive(ServerHost host) throws Exception {
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
						if(!vector.get("tankIDName").isEmpty() && !vector.get("tankIDName").isEmpty()) {

							PlayerData update = playerData.get(myPointer);
							update.username = vector.get("tankIDName");
							playerData.put(myPointer, update);

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
								case "dialog_return":
									{
										// This will be needed for registration/growid.
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "Warning Dialog Return isnt ready."));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
									}
								break;
								case "friends":
									{
										// The friends button.
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnDialogRequest"), "set_default_color|`o\n\nadd_label_with_icon|big|`wSocial Portal``|left|interface/large/friend_button.rttex|0|0|\nadd_spacer|small|\nadd_button|chc0|Show Friends|noflags|0|0|\nadd_button|chc0|Create Guild|noflags|0|0|\nadd_spacer|small||30|\nadd_button|chc0|OK|noflags|0|0|\nnend_dialog|gazette||`yOK|"));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
									}
								break;
								case "eventmenu":
									{
										// The button above friends
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnDialogRequest"), "set_default_color|`o\n\nadd_label_with_icon|big|`7Summer Clash Event Has Ended!|left|836|\nadd_textbox|Summer Clash has ended, but worry not - a new Season Clash is comming soon!|\nadd_spacer|small|\nadd_button|chc0|Close|noflags|0|0|\nnend_dialog|gazette||`yOK|"));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
									}
								break;
								case "store":
									{
										//The store button.
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnStoreRequest"), "set_description_text|Welcome to the `2Growtopia Store``!  Tap the item you'd like more info on.`o  `wWant to get `5Supporter`` status? Any Gem purchase (or `57,000`` Gems earned with free `5Tapjoy`` offers) will make you one. You'll get new skin colors, the `5Recycle`` tool to convert unwanted items into Gems, and more bonuses!\nadd_button|iap_menu|Buy Gems|interface/large/store_buttons5.rttex||0|2|0|0||\nadd_button|subs_menu|Subscriptions|interface/large/store_buttons22.rttex||0|1|0|0||\nadd_button|token_menu|Growtoken Items|interface/large/store_buttons9.rttex||0|0|0|0||\nadd_button|pristine_forceps|`oAnomalizing Pristine Bonesaw``|interface/large/store_buttons20.rttex|Built to exacting specifications by GrowTech engineers to find and remove temporal anomalies from infected patients, and with even more power than Delicate versions! Note : The fragile anomaly - seeking circuitry in these devices is prone to failure and may break (though with less of a chance than a Delicate version)! Use with care!|0|3|3500|0||\nadd_button|itemomonth|`oItem Of The Month``|interface/large/store_buttons16.rttex|`2September 2018:`` `9Sorcerer's Tunic of Mystery!`` Capable of reflecting the true colors of the world around it, this rare tunic is made of captured starlight and aether. If you think knitting with thread is hard, just try doing it with moonbeams and magic! The result is worth it though, as these clothes won't just make you look amazing - you'll be able to channel their inherent power into blasts of cosmic energy!``|0|3|200000|0||\nadd_button|contact_lenses|`oContact Lens Pack``|interface/large/store_buttons22.rttex|Need a colorful new look? This pack includes 10 random Contact Lens colors (and may include Contact Lens Cleaning Solution, to return to your natural eye color)!|0|7|15000|0||\nadd_button|locks_menu|Locks And Stuff|interface/large/store_buttons3.rttex||0|4|0|0||\nadd_button|itempack_menu|Item Packs|interface/large/store_buttons3.rttex||0|3|0|0||\nadd_button|bigitems_menu|Awesome Items|interface/large/store_buttons4.rttex||0|6|0|0||\nadd_button|weather_menu|Weather Machines|interface/large/store_buttons5.rttex|Tired of the same sunny sky?  We offer alternatives within...|0|4|0|0||\n"));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
									}
								break;
								case "killstore":
									{
										//when the store closes
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "Thanks for visiting the store!"));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
									}
								break;
									case "drop":
									{
										//when the user clicks drop
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "Dropping isnt implemented yet."));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
									}
								break;
								case "growid":
									{
										{
										//When you create your account
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "Registration is not supported yet!"));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
										}
										{
										Pack pack = new Pack();
										PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnDialogRequest"), "set_default_color|`o\n\nadd_label_with_icon|big|`wAccount Creation Menu!``|left|32|\n\nadd_spacer|small|\nadd_text_input|username|GrowID: ||15|\nadd_text_input|password|Password: ||100|\nend_dialog|register|Cancel|OK|"));
										Sender sender = new Sender();
										sender.Send(peer, sendData.data);
										}
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
								case "input":
									{
										String message = vector.get("text");
										PlayerInput playerInput = new PlayerInput(peer);
										playerInput.processCommand(message);
										if(message != null && !message.isEmpty()) {
											
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
							PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4Please login with growid"));
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
								PlayerData update = playerData.get(myPointer);
								update.currentWorld = "ABC";
								playerData.put(myPointer, update);

								Sender sender = new Sender();

								GetWorldData worldUtils = new GetWorldData();
								PacketData worldData = worldUtils.GetWorld("lol");
								sender.Send(peer, worldData.data);

								Pack pack = new Pack();
								PacketData mySpawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnSpawn"), "spawn|avatar\nnetID|" + playerData.get(myPointer).netID + "\nuserID|2388\ncolrect|0|0|20|30\nposXY|1611|2018\nname|``" + playerData.get(myPointer).username + "``\ncountry|ru\ninvis|0\nmstate|0\nsmstate|0\ntype|local\n"));
								sender.Send(peer, mySpawnData.data);

								Inventory inv = new Inventory();
								PacketData invData = inv.getInventory();
								sender.Send(peer, invData.data);

								for (Long playerPointer : playerData.keySet()) {
									if(playerPointer != myPointer && playerData.get(playerPointer).currentWorld == "ABC") {
										PacketData spawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnSpawn"), "spawn|avatar\nnetID|" + playerData.get(playerPointer).netID + "\nuserID|2388\ncolrect|0|0|20|30\nposXY|" + playerData.get(playerPointer).x + "|" + playerData.get(playerPointer).y + "\nname|``" + playerData.get(playerPointer).username + "``\ncountry|ru\ninvis|0\nmstate|0\nsmstate|0\n"));
										sender.Send(peer, spawnData.data);
									}
								}
								for (Long playerPointer : playerData.keySet()) {
									if(playerPointer != myPointer && playerData.get(playerPointer).currentWorld == "ABC") {
										ENetPeer playerX = new ENetPeer(playerPointer, false);
										PacketData spawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnSpawn"), "spawn|avatar\nnetID|" + playerData.get(myPointer).netID + "\nuserID|2388\ncolrect|0|0|20|30\nposXY|" + playerData.get(myPointer).x + "|" + playerData.get(myPointer).y + "\nname|``" + playerData.get(myPointer).username + "``\ncountry|ru\ninvis|0\nmstate|0\nsmstate|0\n"));
										sender.Send(playerX, spawnData.data);
									}
								}
							}
							break;
							case "quit_to_exit":
							{	

								PlayerData update = playerData.get(myPointer);
								update.currentWorld = "EXIT";
								playerData.put(myPointer, update);

								Pack pack = new Pack();
								Sender sender = new Sender();
								for (Long playerPointer : playerData.keySet()) {
									if(playerPointer != myPointer) {
										ENetPeer playerX = new ENetPeer(playerPointer, false);
										PacketData spawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnRemove"), "netID|" + playerData.get(myPointer).netID));
										sender.Send(playerX, spawnData.data);
									}
								}

								PacketData sendData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnRequestWorldSelectMenu"), "default|LOL\nadd_button|Showing: `wWorlds``|_catselect_|0.6|3529161471|\nadd_floater|LOL|0|0.55|3529161471\n"));
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

					if(movementData.packetType == 0x00) {
						PlayerData update = playerData.get(myPointer);
						update.x = Math.round(movementData.x);
						update.y = Math.round(movementData.y);
						playerData.put(myPointer, update);

						for (Long playerPointer : playerData.keySet()) {
							if(playerPointer != myPointer) {
								ENetPeer playerX = new ENetPeer(playerPointer, false);
								movementData.netID = playerData.get(myPointer).netID;

								byte[] d = move.packMovement(movementData);
								Sender sender = new Sender();
								PacketData p = new PacketData();
								p.data = d;
								sender.Send(playerX, p.data);
							}
						}
					} 
					if(movementData.punchX != -1 && movementData.punchY != -1) {
						if(movementData.packetType == 3) {
							SendNothingHappened(peer, movementData.punchX, movementData.punchY);
							if(breaks[movementData.punchX][movementData.punchY] < 3 && movementData.plantingTree == 18) {
								for (Long playerPointer : playerData.keySet()) {
									ENetPeer playerX = new ENetPeer(playerPointer, false);
									movementData.packetType = 0x08;
									movementData.plantingTree = 0x04;
									movementData.netID = playerData.get(myPointer).netID;

									byte[] d = move.packMovement(movementData);
									Sender sender = new Sender();
									PacketData p = new PacketData();
									p.data = d;
									sender.Send(playerX, p.data);

								}
								breaks[movementData.punchX][movementData.punchY] += 1;
							} else {
								boolean success = putWorldData.updateTile("test", movementData.plantingTree, movementData.punchX, movementData.punchY);
								if(success == true) {
									for (Long playerPointer : playerData.keySet()) {
										ENetPeer playerX = new ENetPeer(playerPointer, false);
										movementData.netID = playerData.get(myPointer).netID;

										byte[] d = move.packMovement(movementData);
										Sender sender = new Sender();
										PacketData p = new PacketData();
										p.data = d;
										sender.Send(playerX, p.data);

									}
								}
								breaks[movementData.punchX][movementData.punchY] = 0;
							}
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
		long myPointer = peer.getPointer();
		Pack pack = new Pack();
		Sender sender = new Sender();
		for (Long playerPointer : playerData.keySet()) {
			if(playerPointer != myPointer) {
				ENetPeer playerX = new ENetPeer(playerPointer, false);
				PacketData spawnData = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnRemove"), "netID|" + playerData.get(myPointer).netID));
				sender.Send(playerX, spawnData.data);
			}
		}
		playerData.remove(myPointer);
	}

	private void SendNothingHappened(ENetPeer peer, int x, int y) {
		MovementData nothingHappened = new MovementData();
		nothingHappened.packetType = 0x08;
		nothingHappened.plantingTree = 0x00;
		nothingHappened.netID = -1;
		nothingHappened.x = x;
		nothingHappened.y = y;
		nothingHappened.punchX = x;
		nothingHappened.punchY = y;
		Movement move = new Movement();
		byte[] d = move.packMovement(nothingHappened);
		Sender sender = new Sender();
		sender.Send(peer, d);
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