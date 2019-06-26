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

	private int Byte2Int(byte[] bytes) {

		int asInt = (bytes[0] & 0xFF) 
		| ((bytes[1] & 0xFF) << 8) 
		| ((bytes[2] & 0xFF) << 16) 
		| ((bytes[3] & 0xFF) << 24);

		return asInt;

	}
}