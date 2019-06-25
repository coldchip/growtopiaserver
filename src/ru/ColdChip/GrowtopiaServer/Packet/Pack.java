package ru.ColdChip.GrowtopiaServer.Packet;

import  ru.ColdChip.GrowtopiaServer.Utils.Hex2Byte;
import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.PacketData;

public class Pack {
	public PacketData CreatePacket() {
		PacketData data = new PacketData();
		Hex2Byte h2b = new Hex2Byte();
		data.data = h2b.Compute(Constants.PacketHeader);
		data.index = 0;
		return data;
	}

	public PacketData AppendString(PacketData p, String string) {
		int stringLen = string.length();
		byte[] data = new byte[p.data.length + stringLen + 6];
		int type = 2;
		System.arraycopy(p.data, 0, data, 0, p.data.length);
		data[p.data.length] = (byte)p.index;
		data[p.data.length + 1] = (byte)type;
		System.arraycopy(toBytes(stringLen), 0, data, p.data.length + 2, 4);
		System.arraycopy(string.getBytes(), 0, data, p.data.length + 6, stringLen);
		p.data = data;
		int i = p.index+=1;
		p.index = i;
		return p;
	}

	public PacketData AppendInt(PacketData p, int val) {
		byte[] data = new byte[p.data.length + 6];
		int type = 9;
		System.arraycopy(p.data, 0, data, 0, p.data.length);
		data[p.data.length] = (byte)p.index;
		data[p.data.length + 1] = (byte)type;
		System.arraycopy(toBytes(val), 0, data, p.data.length + 2, 4);
		p.data = data;
		int i = p.index+=1;
		p.index = i;
		return p;
	}

	public PacketData PacketEnd(PacketData p) {
		byte[] n = new byte[p.data.length + 1];
		System.arraycopy(p.data, 0, n, 0, p.data.length);
		p.data = n;
		byte[] finalIndex = new byte[1];
		finalIndex[0] = (byte)p.index;
		System.arraycopy(finalIndex, 0, p.data, 56, 1);
		System.arraycopy(finalIndex, 0, p.data, 60, 1);
		return p;
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