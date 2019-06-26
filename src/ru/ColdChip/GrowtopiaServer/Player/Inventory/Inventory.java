package ru.ColdChip.GrowtopiaServer.Player.Inventory;

import ru.ColdChip.GrowtopiaServer.Packet.Structs.*;
import ru.ColdChip.GrowtopiaServer.Utils.*;
import ru.ColdChip.GrowtopiaServer.Packet.Constants;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class Inventory {
	public PacketData getInventory() {
		int inventoryLength = 200;
		byte[] headerData = new Hex2Byte().Compute(Constants.InventoryHeader);
		byte[] data = new byte[headerData.length + (inventoryLength * 4) + 4];

		System.arraycopy(Bswap32fromInt(inventoryLength), 0, headerData, headerData.length - 4, 4);
		System.arraycopy(Bswap32fromInt(inventoryLength), 0, headerData, headerData.length - 8, 4);
		System.arraycopy(headerData, 0, data, 0, headerData.length);
		int val = 0;
		for (int i = 0; i < inventoryLength; i++) {
			val = 0;
			val |= i * 2;
			val |= 200 << 16;
			val &= 0x00FFFFFF;
			val |= 0 << 24;
			System.arraycopy(Int2Byte(val), 0, data, (i*4) + headerData.length, 4);
		}
		PacketData result = new PacketData();
		result.data = data;
		return result;
	}


	private static byte[] Bswap32fromInt(int numero) {
		ByteBuffer bb = ByteBuffer.allocate(4);
		bb.order(ByteOrder.BIG_ENDIAN);
		bb.putInt(numero);
		return bb.array();
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