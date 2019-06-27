/*

	@copyright: ColdChip

*/
package ru.ColdChip.HTTPServlet;

import java.io.IOException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.io.RandomAccessFile;
import ru.ColdChip.ChipSession.SimpleSession;

public class Response {
	public StreamWriter stream;
	private int status = 200;
	private boolean setSession = false;
	private String sessionKey = "";
	private String contentType = "text/html";
	public void WriteHeader(String data) throws IOException {
		data += "\r\n";
		this.stream.write(data.getBytes(), 0, data.length());
	}
	public void WriteByte(byte[] data) throws IOException {
		this.stream.write(data, 0, data.length);
	}
	public void SetSession(String identifier) {
		SimpleSession ss = new SimpleSession();
		String token = ss.GenToken(43200, identifier);
		this.sessionKey = token;
		this.setSession = true;
	}
	public void WriteText(String data) throws IOException {
		String response = "HTTP/1.1 " + this.status + " OK\r\n";
				response += "Content-Length: " + data.length() + "\r\n";
				response += "Content-Type: " + this.contentType + "\r\n";
				response += "Connection: Keep-Alive\r\n";
				response += "Keep-Alive: timeout=5, max=97\r\n";
				response += "Transfer-Encoding: identity\r\n";
				if(this.setSession == true) {
					response += "Set-Cookie: session=" + this.sessionKey + "; path=/; expires=Fri, 31 Dec 9999 23:59:59 GMT\r\n";
				}
				response += "Server: ColdChip Storage 1.0\r\n\r\n" + data;
		this.stream.write(response.getBytes(), 0, response.length());
		this.stream.flush();
	}
	public void Redirect(String loc) throws IOException {
		String response = "HTTP/1.1 302 Found\r\n";
				response += "Content-Length: 0\r\n";
				response += "Content-Type: " + this.contentType + "\r\n";
				response += "Connection: Keep-Alive\r\n";
				response += "Keep-Alive: timeout=5, max=97\r\n";
				response += "Transfer-Encoding: identity\r\n";
				response += "Location: " + loc + "\r\n";
				if(this.setSession == true) {
					response += "Set-Cookie: session=" + this.sessionKey + "; path=/; expires=Fri, 31 Dec 9999 23:59:59 GMT\r\n";
				}
				response += "Server: ColdChip Storage 1.0\r\n\r\n";
		this.stream.write(response.getBytes(), 0, response.length());
		this.stream.flush();
	}
	public void Status(int code) {
		this.status = code;
	}
	public void SetContentType(String contentType) {
		this.contentType = contentType;
	}
	public boolean IsFile(String fileName) {
		File file = new File(fileName);
		if(file.exists() && !file.isDirectory()) {
			return true;
		}
		return false;
	}
	private FileData ReadFile(String path) {
		FileData fd = new FileData();
		try {
			File file = new File(path);
			int fileLength = (int) file.length();
			byte[] byteData = new byte[fileLength];
			FileInputStream fileIn = new FileInputStream(file);
			try {
				fileIn.read(byteData);
				fileIn.close();
				fd.data = byteData;
				fd.length = fileLength;
			} catch (IOException e) {

			}
		} catch(FileNotFoundException e) {

		}
		return fd;
	}
	public String GetMimeType(File file) throws IOException {
		String mimeType = Files.probeContentType(file.toPath());
		if(mimeType == null) {
			return "application/octet-stream";
		} else {
			return mimeType;
		}
	}
	public void WriteFile(String path) throws IOException {
		if(IsFile(path) == true) {
			SetContentType(GetMimeType(new File(path)));
			RandomAccessFile rFile = new RandomAccessFile(new File(path), "r");
			String response = "HTTP/1.1 " + this.status + " OK\r\n";
				  response += "Content-Length: " + rFile.length() + "\r\n";
				  response += "Content-Type: " + this.contentType + "\r\n";
				  response += "Connection: Keep-Alive\r\n";
				  response += "Keep-Alive: timeout=5, max=97\r\n";
				  response += "Transfer-Encoding: identity\r\n";
				  response += "Server: ColdChip Storage 1.0\r\n\r\n";
			this.stream.write(response.getBytes(), 0, response.length());
			this.stream.flush();
			byte[] b = new byte[8192];
			long i = 0;
			int size = 0;
			while((size = rFile.read(b, 0, 8192)) != -1) {
				this.stream.write(b, 0, size);
				this.stream.flush();
			}
		} else {
			WriteText("File Not Found");
		}
	}
}