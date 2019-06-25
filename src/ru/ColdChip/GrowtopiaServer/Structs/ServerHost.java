package ru.ColdChip.GrowtopiaServer.Structs;

import ru.ColdChip.GrowtopiaServer.ENetJava.ENetAddress;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetEvent;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetHost;
import ru.ColdChip.GrowtopiaServer.ENetJava.ENetPeer;

public class ServerHost {
	public ENetEvent event;
	public ENetAddress address;
	public ENetHost host;
	public ENetPeer peer;

}