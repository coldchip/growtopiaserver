package ru.ColdChip.GrowtopiaServer.Player.Movement;

import ru.ColdChip.GrowtopiaServer.Player.Movement.Structs.MovementData;
import ru.ColdChip.GrowtopiaServer.Packet.Structs.PacketData;
import java.util.Arrays;

public class Movement {
	public MovementData unpackMovement(byte[] data) {
		MovementData movementData = new MovementData();
		movementData.packetType = (Byte2Int(Arrays.copyOfRange(data, 0, 4)));
		movementData.netID = (Byte2Int(Arrays.copyOfRange(data, 4, 8)));
		movementData.characterState = (Byte2Int(Arrays.copyOfRange(data, 12, 16)));
		movementData.plantingTree = (Byte2Int(Arrays.copyOfRange(data, 20, 24)));
		movementData.x = (Float.intBitsToFloat(Byte2Int(Arrays.copyOfRange(data, 24, 28))));
		movementData.y = (Float.intBitsToFloat(Byte2Int(Arrays.copyOfRange(data, 28, 32))));
		movementData.XSpeed = (Float.intBitsToFloat(Byte2Int(Arrays.copyOfRange(data, 32, 36))));
		movementData.YSpeed = (Float.intBitsToFloat(Byte2Int(Arrays.copyOfRange(data, 36, 40))));
		movementData.punchX = (Byte2Int(Arrays.copyOfRange(data, 44, 48)));
		movementData.punchY = (Byte2Int(Arrays.copyOfRange(data, 48, 52)));
		return movementData;
	}

	public byte[] packMovement(MovementData mData) {
		byte[] data = new byte[61];
		System.arraycopy(toBytes(4), 0, data, 0, 4);
		System.arraycopy(toBytes(mData.packetType), 0, data, 4, 4);
		System.arraycopy(toBytes(mData.netID), 0, data, 8, 4);
		System.arraycopy(toBytes(mData.characterState), 0, data, 16, 4);
		System.arraycopy(toBytes(mData.plantingTree), 0, data, 24, 4);

		System.arraycopy(toBytes(Float.floatToIntBits(mData.x)), 0, data, 28, 4);
		System.arraycopy(toBytes(Float.floatToIntBits(mData.y)), 0, data, 32, 4);
		System.arraycopy(toBytes(Float.floatToIntBits(mData.XSpeed)), 0, data, 36, 4);
		System.arraycopy(toBytes(Float.floatToIntBits(mData.YSpeed)), 0, data, 40, 4);
		
		System.arraycopy(toBytes(mData.punchX), 0, data, 48, 4);
		System.arraycopy(toBytes(mData.punchY), 0, data, 52, 4);
		return data;
	}

	private int Byte2Int(byte[] bytes) {

		int asInt = (bytes[0] & 0xFF) 
		| ((bytes[1] & 0xFF) << 8) 
		| ((bytes[2] & 0xFF) << 16) 
		| ((bytes[3] & 0xFF) << 24);

		return asInt;

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