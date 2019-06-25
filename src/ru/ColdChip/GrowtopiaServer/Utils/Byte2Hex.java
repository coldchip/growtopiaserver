package ru.ColdChip.GrowtopiaServer.Utils;

public class Byte2Hex {
	public String Compute(byte[] hashInBytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < hashInBytes.length; i++) {
            String hex = Integer.toHexString(0xff & hashInBytes[i]);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
	}
}