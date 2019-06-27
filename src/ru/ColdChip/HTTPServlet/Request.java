/*

	@copyright: ColdChip

*/
package ru.ColdChip.HTTPServlet;

import java.util.*;
import java.net.HttpCookie;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class Request {
	public Header header;
	public StreamReader stream;
	public String GetValue(String key) {
		String results = "";
		if(this.header.query.containsKey(key) == true) {
			results = this.header.query.get(key);
		}
		return results;
	}
	public String GetPostValue(String key) {
		String results = "";
		if(this.header.postQuery.containsKey(key) == true) {
			results = this.header.postQuery.get(key);
		}
		return results;
	}
	public String GetSession() {
		return GetCookie("session");
	}
	public String GetHeader(String key) {
		key = key.toLowerCase();
		String results = new String();
		if(this.header.headers.containsKey(key) == true) {
			results = this.header.headers.get(key).replaceAll("\r", "");
		}
		return results;
	}
	public String GetCookie(String key) {
		String command = "(.*)" + key + "=([^;]*)(.*)";
		String input = GetHeader("cookie");
		Pattern pattern = Pattern.compile(command);
		Matcher matcher = pattern.matcher(input);
        if (matcher.matches()) {
        	return matcher.group(2);
        }
		return "";
	}
}