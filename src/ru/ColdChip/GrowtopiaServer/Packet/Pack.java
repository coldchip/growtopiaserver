package ru.ColdChip.GrowtopiaServer.Packet;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.PacketData;

public class Pack {
	public PacketData CreatePacket() {
		byte[] data = new byte[61];

		PacketData packet = new PacketData();
		packet.data = Hex2Byte(Constants.PacketHeader);
		System.out.println(new String(packet.data).length());
		packet.length = 61;
		packet.indexes = 0;
		return packet;
	}

	public PacketData AppendString(PacketData p, String str) {
		byte[] n = new byte[p.length + 2 + str.length() + 4];
		System.arraycopy(n, 0, p.data, 0, p.length);
		p.data = n;
		p.data[p.length] = (byte)p.indexes;
		p.data[p.length + 1] = 2;
		int sLen = str.length();
		System.arraycopy(p.data, p.length + 2, toBytes(sLen), 0, 4);
		System.arraycopy(p.data, p.length + 6, str.getBytes(), 0, sLen);
		p.length = p.length + 2 + str.length() + 4;
		p.indexes++;
		return p;
	}

	public PacketData PacketEnd(PacketData p) {
		byte[] n = new byte[p.length + 1];
		System.arraycopy(n, 0, p.data, 0, p.length);
		p.data = n;
		byte zero = 0;
		p.data[p.length] = zero;
		p.length += 1;
		System.arraycopy(p.data, 56, toBytes(p.indexes), 0, 4);
		System.arraycopy(p.data, 60, toBytes(p.indexes), 0, 4);
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