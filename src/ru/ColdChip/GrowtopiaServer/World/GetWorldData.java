package ru.ColdChip.GrowtopiaServer.World;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Utils.*;
import ru.ColdChip.GrowtopiaServer.Packet.Constants;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.PacketData;

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;

public class GetWorldData {
	public PacketData GetWorld(String name) {
		try {
			byte[] worldData = GetWorldFile("test");
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
				byte[] blk = new byte[2];
				System.arraycopy(worldData, i, blk, 0, 2);
				System.arraycopy(blk, 0, data, headerData.length + 2 + name.length() + 4 + 4 + 4 + i, 2);
				int type = 0;
				if(square - i <= 700) {
					block = 8;
				} else {
					block = 0;
				}
				type |= 0;
				System.arraycopy(Int2Byte(type), 0, data, headerData.length + 2 + name.length() + 4 + 4 + 4 + i + 4, 4);
			}

			System.arraycopy(Int2Byte(0), 0, data, headerData.length - 4, 4);
			PacketData pData = new PacketData();
			pData.data = data;
			return pData;
		} catch(IOException e) {
			PacketData pData = new PacketData();
			return pData;
		}
	}

	private byte[] GetWorldFile(String name) throws IOException {
		try {
			RandomAccessFile f = new RandomAccessFile("Database/Worlds/" + name, "r");
			byte[] b = new byte[(int)f.length()];
			f.readFully(b);
			return b;
		} catch(FileNotFoundException e) {
			RandomAccessFile f = new RandomAccessFile("Database/Worlds/" + name, "rw");
			short block = 0;

			int xSize = 100;
			int ySize = 50;
			int square = xSize * ySize;

			for(int i = 0; i < square * 8; i+=8) {
				byte[] data = new byte[8];
				System.arraycopy(Short2Byte(block), 0, data, 0, 2);
				int type = 0;
				if(square - i <= 700) {
					block = 8;
				} else {
					block = 0;
				}
				type |= 0;
				System.arraycopy(Int2Byte(type), 0, data, 4, 4);
				f.write(data, 0, 8);
			}
			return GetWorldFile(name);
		}
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