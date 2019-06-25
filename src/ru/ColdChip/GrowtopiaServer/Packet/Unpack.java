package ru.ColdChip.GrowtopiaServer.Packet;

import java.util.Arrays;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;

public class Unpack {
	public String UnpackTextPacket(byte[] data) {
		int type = 	(data[0] & 0xFF) & 255 | 
					((data[1] & 0xFF) << 8) & 255 | 
					((data[2] & 0xFF) << 16 ) & 255 | 
					((data[3] & 0xFF) << 24 ) & 255;
		return new String(Arrays.copyOfRange(data, 4, data.length));
	}
}