/*

	@copyright: ColdChip

*/
package ru.ColdChip.HTTPServlet;

import java.io.OutputStream;
import java.io.IOException;
import java.util.concurrent.*;
import java.lang.InterruptedException;

public class StreamWriter {

	private OutputStream stream;

	private boolean isTimeout = false;

	public StreamWriter(OutputStream stream) {
		this.stream = stream;
	}

	public void write(byte[] b) throws IOException {
        this.stream.write(b);
	}

	public void write(byte[] b, int offset, int length) throws IOException {
        this.stream.write(b, offset, length);
	}

	public void close() throws IOException {
		this.stream.close();
	}

	public void flush() throws IOException {
		this.stream.flush();
	}
}