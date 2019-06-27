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
import java.util.concurrent.TimeUnit;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import javax.net.ServerSocketFactory;
import javax.net.ssl.SSLServerSocketFactory;
import java.util.HashMap;

public class HTTPServer { 

	private int PORT = 9012;

	private HashMap<String, Route> map = new HashMap<>();

	private Route notFound = new Route() {
		@Override
		public void handle(Request request, Response response) throws IOException {
			response.Status(404);
			response.WriteText("404 Not Found");
		}
	};

	private Socket connect;
	
	public HTTPServer(int port) {
		this.PORT = port;
	}

	public void Req(String path, Route route) {
		map.put(path, route);
	}

	public void NotFound(Route route) {
		notFound = route;
	}

	public void run() {
		int port = this.PORT;
		Thread http = new Thread() {
			public void run() {
				try {
					ServerSocket serverConnect = new ServerSocket(port);
					System.out.println("Server started.\nListening for connections on port: " + port + "\n");
					System.out.println("\033[32m[Thread] \033[30m\033[42mHTTP Server Started [Pre Thread Spawn]\033[0m\r\n\r\n");
					while (true) {
						HTTPServerThread myServer = new HTTPServerThread(map, notFound);
						Thread thread = new Thread(myServer);
						Socket client = serverConnect.accept();
						myServer.AttachClient(client);
						thread.start();
					}
				} catch (IOException e) {
					System.err.println("Server Connection error: " + e.getMessage());
				}
			}
		};
		http.start();
	}

	public void WriteHeader(StreamWriter out, String data) throws IOException {
		data += "\r\n";
		out.write(data.getBytes(), 0, data.length());
	}
	
}