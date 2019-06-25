package ru.ColdChip.GrowtopiaServer.World;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Utils.*;
import ru.ColdChip.GrowtopiaServer.Packet.Constants;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.PacketData;

public class GetWorldData {
	public PacketData GetWorld(String name) {
		int xSize = 100;
		int ySize = 50;
		int square = xSize * ySize;

		byte[] headerData = new Hex2Byte().Compute(Constants.WorldHeader);
		byte[] data = new byte[headerData.length + 2 + name.length() + 12 + (square * 8) + 4];
		
		System.arraycopy(headerData, 0, data, 0, headerData.length);
		System.arraycopy(Short2Byte((short)name.length()), 0, data, headerData.length, 2);
		System.arraycopy(name.getBytes(), 0, data, headerData.length + 2, name.length());
		System.arraycopy(Int2Byte(xSize), 0, data, headerData.length + 2 + name.length(), 4);
		System.arraycopy(Int2Byte(ySize), 0, data, headerData.length + 2 + name.length() + 4, 4);
		System.arraycopy(Int2Byte(square), 0, data, headerData.length + 2 + name.length() + 4 + 4, 4);

		short block = 0;

		for(int i = 0; i < square * 8; i+=8) {
			System.arraycopy(Short2Byte(block), 0, data, headerData.length + 2 + name.length() + 4 + 4 + 4 + i, 2);
			int type = 0;
			type |= 0;
			System.arraycopy(Int2Byte(type), 0, data, headerData.length + 2 + name.length() + 4 + 4 + 4 + i + 4, 4);
		}

		System.arraycopy(Int2Byte(0), 0, data, headerData.length - 4, 4);

		PacketData pData = new PacketData();
		pData.data = data;
		return pData;
	}

	private byte[] Short2Byte(short val) {
		byte[] ret = new byte[2];
		ret[0] = (byte)(val & 0xff);
		ret[1] = (byte)((val >> 8) & 0xff);
		return ret;
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