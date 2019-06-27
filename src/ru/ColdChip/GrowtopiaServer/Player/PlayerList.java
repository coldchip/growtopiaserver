package ru.ColdChip.GrowtopiaServer.Player;

import ru.ColdChip.GrowtopiaServer.Player.Structs.*;

import java.util.*;

public class PlayerList {
	public static HashMap<Long, PlayerData> playerData = new HashMap<Long, PlayerData>();

	public PlayerData get(Long key) {
		return this.playerData.get(key);
	}

	public PlayerData put(Long key, PlayerData value) {
		return this.playerData.put(key, value);
	}

	public Set<Long> keySet() {
		return this.playerData.keySet();
	}

	public void remove(Long key) {
		this.playerData.remove(key);
	}
}