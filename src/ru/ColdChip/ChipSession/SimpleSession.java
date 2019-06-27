/*

	@copyright: ColdChip

*/
package ru.ColdChip.ChipSession;

import java.util.*;

public class SimpleSession {

	private static HashMap<String, String> tokens = new HashMap<>();

	private static HashMap<String, Integer> expiry = new HashMap<>();

	private static HashMap<String, HashMap<Integer, String>> a = new HashMap<>();

	private String randomString(int length) {
		final String list = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		StringBuilder results = new StringBuilder();
		for(int i = 0; i < length; i++) {
			int character = (int)(Math.random()*list.length());
			results.append(list.charAt(character));
		}
		return results.toString();
	}

	public String GenToken(int expire, String data) {
		GarbageCollector();
		if(this.tokens.containsValue(data)) {
			Iterator<String> tokenIterator = this.tokens.keySet().iterator();
			while (tokenIterator.hasNext()) {
				String key = tokenIterator.next();
				if(this.tokens.containsValue(data) == true) {
					this.expiry.put(key, (GetTime() + expire));
					return key;
				}
			}
			return "0";
		} else {
			String random = randomString(128);
			this.tokens.put(random, data);
			this.expiry.put(random, (GetTime() + expire));
			return random;
		}
	}

	public boolean Validate(String token) {
		GarbageCollector();
		if(this.tokens.containsKey(token) == true) {
			return true;
		}
		return false;
	}
	public String GetTokenData(String token) {
		GarbageCollector();
		if(this.tokens.containsKey(token) == true) {
			return this.tokens.get(token);
		}
		return new String();
	}
	private void GarbageCollector() {
		ArrayList<String> toRemove = new ArrayList<String>();

		Iterator<String> expiryIterator = this.expiry.keySet().iterator();
		while (expiryIterator.hasNext()) {
			String key = expiryIterator.next();
			if(this.expiry.containsKey(key) == true) {
				int expire = this.expiry.get(key);
				if(GetTime() > expire) {
					expiryIterator.remove();
					toRemove.add(key);
				}
			}
		}

		Iterator<String> tokenIterator = this.tokens.keySet().iterator();
		while (tokenIterator.hasNext()) {
			String key = tokenIterator.next();
			if(this.tokens.containsKey(key) == true) {
				if(toRemove.contains(key)) {
					tokenIterator.remove();
				}
			}
		} 
	}
	private int GetTime() {
		return (int)(System.currentTimeMillis() / 1000);
	}
}