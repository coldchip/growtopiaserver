/*

	@copyright: ColdChip

*/
package ru.ColdChip.HTTPServlet;

import java.util.*;

public class Header {
	public String method;
	public String path;
	public String version;
	public HashMap<String, String> postQuery = new HashMap<String, String>();
	public HashMap<String, String> query = new HashMap<String, String>();
	public HashMap<String, String> headers = new HashMap<String, String>();
}