package ru.ColdChip.GrowtopiaServer.Utils;

import java.util.*;

public class Vectorize {
	private HashMap<String, String> data = new HashMap<String, String>();
	public Vectorize(String data) {
		Scanner scanner = new Scanner(data);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			String[] parts = line.split("\\|");
			if(parts.length == 2) {
				String key = parts[0];
				String value = parts[1];
				if(line.contains("|")) {
					this.data.put(key, value);
				}
			}
		}
	}
	public boolean containsKey(String key) {
		return this.data.containsKey(key);
	}

	public String get(String key) {
		return this.data.get(key);
	}
}