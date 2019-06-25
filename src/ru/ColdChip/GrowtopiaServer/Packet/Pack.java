package ru.ColdChip.GrowtopiaServer.Packet;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.PacketData;

public class Pack {
	public PacketData CreatePacket() {
		PacketData data = new PacketData();
		data.data = Hex2Byte(Constants.PacketHeader);
		data.index = 0;
		return data;
	}

	public PacketData AppendString(PacketData p, String string) {
		int stringLen = string.length();
		byte[] data = new byte[p.data.length + stringLen + 6];
		int type = 2;
		int i = p.index++;
		System.arraycopy(p.data, 0, data, 0, p.data.length);
		//System.arraycopy(p.data, 0, data.data, 0, p.data.length);
		data[p.data.length] = (byte)i;
		data[p.data.length + 1] = (byte)type;
		System.arraycopy(toBytes(stringLen), 0, data, p.data.length + 2, 4);
		p.data = data;
		p.index = i;
		return p;
	}

	public PacketData PacketEnd(PacketData p) {
		byte[] n = new byte[p.length + 1];
		System.arraycopy(n, 0, p.data, 0, 1);
		p.data = n;
		byte zero = 0;
		p.data[p.length] = zero;
		p.length += 1;
		System.arraycopy(toBytes(p.index), 0, p.data, 56, 4);
		System.arraycopy(toBytes(p.index), 0, p.data, 60, 4);
		return p;
	}

	private static byte[] Hex2Byte(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}

	byte[] toBytes(int i) {
		byte[] result = new byte[4];

		result[0] = (byte) (i >> 24);
		result[1] = (byte) (i >> 16);
		result[2] = (byte) (i >> 8);
		result[3] = (byte) (i /*>> 0*/);

		return result;
	}
}