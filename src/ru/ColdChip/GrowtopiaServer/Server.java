/*

	@copyright: ColdChip

*/

package ru.ColdChip.GrowtopiaServer;

import ru.ColdChip.GrowtopiaServer.ENetJava.enet;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetHost;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetPeer;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetEvent;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetAddress;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetEventType;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetPacket;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetPacketFlag;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetBuffer;
import ru.ColdChip.GrowtopiaServer.Packet.Sender;

import ru.ColdChip.GrowtopiaServer.Packet.Structs.*;
import ru.ColdChip.GrowtopiaServer.Packet.Pack;
import ru.ColdChip.GrowtopiaServer.Player.PlayerList;
import ru.ColdChip.GrowtopiaServer.Structs.ServerHost;
import ru.ColdChip.GrowtopiaServer.Events.ServerEvent;
import ru.ColdChip.GrowtopiaServer.GUI.MainView;

import ru.ColdChip.HTTPServlet.HTTPServer;
import ru.ColdChip.HTTPServlet.Route;
import ru.ColdChip.HTTPServlet.Request;
import ru.ColdChip.HTTPServlet.Response;

import java.nio.file.Paths;
import java.io.IOException;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.nio.file.Path;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.lang.InterruptedException;

public class Server { 

	private static boolean status = true;

	private static boolean restart = false;

	private static int restartMode = 0;

	private static long restartTime = 0;

	public static void main(String[] args) {
		System.out.println("\r\n\r\n\r\n\r\n\033[32m[Thread] \033[30m\033[42mGrowtopia Server Started\033[0m\r\n\r\n");
		if (GraphicsEnvironment.isHeadless()) {
			// non gui mode
			System.out.println("NON-GUI");
		} else {
			// gui mode
			System.out.println("GUI");
			MainView mainView = new MainView();
		}

		HTTPServer s = new HTTPServer(9010);
		s.Req("/.*", new Route() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				String path = PathNormalize(request.header.path);
				File target = new File(PathNormalize(Paths.get("").toAbsolutePath().toString() + "/html/" + path));
				if(target.exists() == true && target.isDirectory() == false) {
					response.WriteFile(PathNormalize(Paths.get("").toAbsolutePath().toString() + "/html/" + path));
				} else {
					response.WriteFile(PathNormalize(Paths.get("").toAbsolutePath().toString() + "/html/" + path + "/index.html"));
				}
			}
		});
		s.Req("/api/v1/status", new Route() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				if(Server.restartMode != 0) {
					response.WriteText("{\"status\":2, \"time\":" + Server.restartTime + "}");
				} else {
					if(Server.status == true) {
						response.WriteText("{\"status\":0}");
					} else {
						response.WriteText("{\"status\":1}");
					}
				}
			}
		});
		s.Req("/api/v1/serverOnOff", new Route() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				response.WriteText("{\"status\":\"ok\"}");
				if(Server.status == false) {
					Server.status = true;
				} else {
					Server.status = false;
				}

			}
		});
		s.Req("/api/v1/serverRestart", new Route() {
			@Override
			public void handle(Request request, Response response) throws IOException {
				response.WriteText("{\"status\":\"ok\"}");
				if(Server.restart == false) {
					Server.restart = true;
				} else {
					Server.restart = false;
				}

			}
		});
		s.run();
		Thread server = new Thread() {
			public void run() {
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
							if(Server.restart == true) {
								PlayerList playerData = new PlayerList();
								Pack pack = new Pack();
								Sender sender = new Sender();
								for (Long playerPointer : playerData.keySet()) {
									ENetPeer playerX = new ENetPeer(playerPointer, false);
									PacketData msg = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4Global System Message: ``Restarting for update in `41 ``minute. "));
									sender.Send(playerX, msg.data);
									PacketData music = pack.PacketEnd(pack.AppendInt(pack.AppendString(pack.AppendString(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnAddNotification"), "interface/science_button.rttex"), "Restarting soon!"), "audio/mp3/suspended.mp3"), 0));
									sender.Send(playerX, music.data);
								}
								Server.restart = false;
								Server.restartMode = 1;
								Server.restartTime = Long.valueOf(String.valueOf(System.currentTimeMillis()/1000));
							}
							if(Server.restartMode == 1 && (Long.valueOf(String.valueOf(System.currentTimeMillis()/1000)) - Server.restartTime) >= 30) {
								PlayerList playerData = new PlayerList();
								Pack pack = new Pack();
								Sender sender = new Sender();
								for (Long playerPointer : playerData.keySet()) {
									ENetPeer playerX = new ENetPeer(playerPointer, false);
									PacketData msg = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4Global System Message: ``Restarting for update in `430 ``seconds. "));
									sender.Send(playerX, msg.data);
								}
								Server.restartMode = 2;
							}
							if(Server.restartMode == 2 && (Long.valueOf(String.valueOf(System.currentTimeMillis()/1000)) - Server.restartTime) >= 62) {
								PlayerList playerData = new PlayerList();
								Pack pack = new Pack();
								Sender sender = new Sender();
								for (Long playerPointer : playerData.keySet()) {
									ENetPeer playerX = new ENetPeer(playerPointer, false);
									PacketData msg = pack.PacketEnd(pack.AppendString(pack.AppendString(pack.CreatePacket(), "OnConsoleMessage"), "`4Global System Message: ``Restarting for update in `4ZERO ``second. Should be back up in a minute or so. BYE"));
									sender.Send(playerX, msg.data);
								}
								Server.restartMode = 0;
								Server.status = false;
							}
							if(en.Service(host.host, host.event, 10) > 0 && Server.status == true) {
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
							StringWriter writer = new StringWriter();
							PrintWriter printWriter = new PrintWriter( writer );
							e.printStackTrace( printWriter );
							printWriter.flush();

							String stackTrace = writer.toString();
					    	System.err.println("ENet: " + stackTrace);
					    }
					}
			    } catch (UnsatisfiedLinkError e) {
					System.err.println("Native code library failed to load.\n" + e);
					System.exit(1);
				}
			}
		};
		server.start();

	}

	public static String PathNormalize(String path) {
		Path result = Paths.get("/", path).normalize();
		return result.toString();
	}

}