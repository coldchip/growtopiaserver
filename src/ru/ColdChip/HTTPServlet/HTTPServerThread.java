/*

	@copyright: ColdChip

*/
package ru.ColdChip.HTTPServlet;

import java.io.BufferedOutputStream;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.lang.NumberFormatException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.nio.charset.StandardCharsets;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.concurrent.TimeUnit;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.util.HashMap;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.*;  

public class HTTPServerThread implements Runnable { 

	private Socket connect;

	private HashMap<String, Route> map = new HashMap<>();

	private Route notFound = new Route();

	public HTTPServerThread(HashMap<String, Route> map, Route notFound) {
		this.map = map;
		this.notFound = notFound;
	}

	public void AttachClient(Socket c) {
		connect = c;
	}

	@Override
	public void run() {
		StreamReader input = null;
		StreamWriter output = null;
		try {
			connect.setSoTimeout(30000);
			input = new StreamReader(connect.getInputStream());
			output = new StreamWriter(connect.getOutputStream());
			while(true) {
				Header headerLine = ReadHeader(input);
				if(headerLine.method.equals("POST")) {
					if(headerLine.headers.containsKey("content-length") && headerLine.headers.containsKey("content-type")) {
						String contentLength = headerLine.headers.get("content-length");
						String contentType = headerLine.headers.get("content-type");
						if(contentType.equals("application/x-www-form-urlencoded")) {
							try {
								int contentSize = Integer.parseInt(contentLength.replaceAll("[^0-9]", ""));
								if(contentSize > 0 && contentSize < 16000) {
									byte[] data = new byte[contentSize];
									int read = input.read(data, 0, contentSize);
									String queryData = new String(data);
									headerLine.postQuery = SplitQuery(queryData);
								}
							} catch(NumberFormatException e) {
								Response responseError = new Response();
								responseError.stream = output;
								responseError.WriteText("Invalid Content-Length");
							}
						}
					}
				}
				
				Request request = new Request();
				request.header = headerLine;
				request.stream = input;

				Response response = new Response();
				response.stream = output;
				try {
					for (String key : this.map.keySet()) {
						if(Pattern.matches(key, headerLine.path)) {
							Route route = new Route();
							route = this.map.get(key);
							route.handle(request, response);
							break;
						}
					}
				} catch(IOException e) {
					response.WriteText("500 Internal Server Error");
				} catch(Exception e) {
					this.notFound.handle(request, response);
				}
			}
		} catch (Exception e) {
			// System.err.println("Unknown Exception: " + e);
		} finally {
			try {
				input.close();
				output.close();
				connect.close();
			} catch (Exception e) {
				
			}
		}
	}

	private String readLine(StreamReader in) throws IOException {
		String result = new String();
		while(true) {
			byte [] bit = new byte[1];
			in.read(bit);
			if(bit[0] == 0x0a) {
				break;
			} else {
				result += (char)bit[0];
			}
		}
		return result;
	}

	private Header ReadHeader(StreamReader in) throws IOException, UnsupportedEncodingException {
		Header head = new Header();
		String[] headerLine = new String[1024];
		int i = 0;
		while(true) {
			headerLine[i] = readLine(in);
			if(i > 1 && headerLine[i].length() > 0) {
				StringTokenizer headerValues = new StringTokenizer(headerLine[i], ":");
				String key = headerValues.nextToken().toLowerCase();
				if(headerValues.countTokens() == 1) {
					String value = ltrim(headerValues.nextToken());
					// Remove 0x0d
					value = value.substring(0, value.length() - 1);
					if(key.length() > 0 && value.length() > 0) {
						head.headers.put(key, value);
					}
				}
			}
			if(headerLine[i].length() == 1) {
				break;
			} else {
				if(i >= headerLine.length - 1) {
					throw new IOException("Header too long. ");
				} else {
					i++;
				}
			}
		}
		StringTokenizer parse = new StringTokenizer(headerLine[0]);
		head.method = parse.nextToken().toUpperCase();
		StringTokenizer pathData = new StringTokenizer(parse.nextToken(), "?");
		head.path = URLDecodePath(pathData.nextToken());
		if(pathData.countTokens() == 1) {
			head.query = SplitQuery(pathData.nextToken());
		}
		head.version = parse.nextToken().toLowerCase();
		return head;
	}

	public static String ltrim(String s) {
	    int i = 0;
	    while (i < s.length() && Character.isWhitespace(s.charAt(i))) {
	        i++;
	    }
	    return s.substring(i);
	}

	private String URLDecodePath(String url) throws UnsupportedEncodingException {
		return URLDecoder.decode(url, "UTF-8");
	}

	public String PathNormalize(String path) {
		Path result = Paths.get("/", path).normalize();
		return result.toString();
	}
	
	private String getContentType(String fileRequested) {
		if (fileRequested.endsWith(".htm")  ||  fileRequested.endsWith(".html")) {
			return "text/html";
		} else {
			return "text/plain";
		}
	}

	public static HashMap<String, String> SplitQuery(String query) throws UnsupportedEncodingException {
		HashMap<String, String> query_pairs = new LinkedHashMap<String, String>();
		String[] pairs = query.split("&");
		for (String pair : pairs) {
		    int idx = pair.indexOf("=");
		    query_pairs.put(URLDecoder.decode(pair.substring(0, idx), "UTF-8"), URLDecoder.decode(pair.substring(idx + 1), "UTF-8"));
		}
		return query_pairs;
	}
	
}