package ru.ColdChip.GrowtopiaServer.World;

import ru.ColdChip.GrowtopiaServer.ENetJava.*;
import ru.ColdChip.GrowtopiaServer.Utils.*;
import ru.ColdChip.GrowtopiaServer.Packet.Constants;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.PacketData;

import java.io.RandomAccessFile;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class GetWorldData {
	public PacketData GetWorld(String name) {
		try {
			byte[] worldData = GetWorldFile("test");
			int xSize = 100;
			int ySize = 100;
			int square = xSize * ySize;

			byte[] headerData = new Hex2Byte().Compute(Constants.WorldHeader);
			byte[] data = new byte[headerData.length + 2 + name.length() + 12 + (square * 8) + 4];
			
			System.arraycopy(headerData, 0, data, 0, headerData.length);
			System.arraycopy(Short2Byte((short)name.length()), 0, data, headerData.length, 2);
			System.arraycopy(name.getBytes(), 0, data, headerData.length + 2, name.length());
			System.arraycopy(Int2Byte(xSize), 0, data, headerData.length + 2 + name.length(), 4);
			System.arraycopy(Int2Byte(ySize), 0, data, headerData.length + 2 + name.length() + 4, 4);
			System.arraycopy(Int2Byte(square), 0, data, headerData.length + 2 + name.length() + 4 + 4, 4);

			for(int i = 0; i < square * 8; i+=8) {
				System.arraycopy(worldData, i, data, headerData.length + 2 + name.length() + 4 + 4 + 4 + i, 2);
				System.arraycopy(worldData, i + 2, data, headerData.length + 2 + name.length() + 4 + 4 + 4 + i + 2, 2);
				System.arraycopy(Int2Byte(0), 0, data, headerData.length + 2 + name.length() + 4 + 4 + 4 + i + 4, 4);
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

	public int getBlockAt(int x, int y) throws IOException {
		byte[] data = GetWorldFile("test");
		int index = ((100 * y) + x) * 8;
		byte[] item = new byte[2];
		System.arraycopy(data, index, item, 0, 2);
		return (int)fromByteArray(item);
	}

	public int getBackgroundAt(int x, int y) throws IOException {
		byte[] data = GetWorldFile("test");
		int index = ((100 * y) + x) * 8;
		byte[] item = new byte[2];
		System.arraycopy(data, index + 2, item, 0, 2);
		return (int)fromByteArray(item);
	}

	private byte[] GetWorldFile(String name) throws IOException {
		try {
			RandomAccessFile f = new RandomAccessFile("Database/Worlds/" + name, "r");
			byte[] b = new byte[(int)f.length()];
			f.readFully(b);
			return b;
		} catch(FileNotFoundException e) {
			RandomAccessFile f = new RandomAccessFile("Database/Worlds/" + name, "rw");
			int xSize = 100;
			int ySize = 100;
			int square = xSize * ySize;

			for(int i = 0; i < square; i++) {
				byte[] data = new byte[8];
				short block = 0;
				short background = 0;
				if(i == 6350) {
					background = 6;
				} else if (i >= 6400 && i <= 6600) {
					if(i == 6450) {
						block = 8;
					} else {
						block = 2;
					}
					background = 14;
				} else if(i >= 6600 && i <= 9000) {
					if(Math.floor(Math.random() * 100) % 50 == 0) {
						block = 10;
					} else {
						block = 2;
					}
					background = 14;
				} else if(i > 9000 && i <= 9400) {
					if(Math.floor(Math.random() * 100) % 7 == 0) {
						block = 4;
					} else {
						block = 2;
					}
					background = 14;
				} else if(i > 9400) {
					block = 8;
				} else {
					block = 0;
				}
				System.arraycopy(Short2Byte(block), 0, data, 0, 2);
				System.arraycopy(Short2Byte(background), 0, data, 2, 2);
				System.arraycopy(Int2Byte(0), 0, data, 4, 4);
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

	short fromByteArray(byte[] bytes) {
		ByteBuffer bb = ByteBuffer.allocate(2);
		bb.order(ByteOrder.LITTLE_ENDIAN);
		bb.put(bytes[0]);
		bb.put(bytes[1]);
		return bb.getShort(0);
	}
}