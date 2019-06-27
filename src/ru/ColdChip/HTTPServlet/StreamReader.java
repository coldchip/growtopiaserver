/*

	@copyright: ColdChip

*/
package ru.ColdChip.HTTPServlet;

import java.io.InputStream;
import java.io.IOException;
import java.util.concurrent.*;
import java.lang.InterruptedException;

public class StreamReader {

	private InputStream stream;

	private boolean isTimeout = false;

	public StreamReader(InputStream stream) {
		this.stream = stream;
	}

	public int read(byte[] b) throws IOException {
		int length = this.stream.read(b);
		if(length == -1) {
			throw new IOException("End of stream");
		} else {
			return length;
		}
	}

	public int read(byte[] b, int offset, int length) throws IOException {
        int len = this.stream.read(b, offset, length);
        if(len == -1) {
			throw new IOException("End of stream");
		} else {
			return len;
		}
	}

	public int available() throws IOException {
		return this.stream.available();
	}

	public void close() throws IOException {
		this.stream.close();
	}
}