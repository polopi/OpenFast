package org.openfast.test;

import java.io.IOException;
import java.io.OutputStream;

public class IOExceptionOnCloseStream extends OutputStream {

	@Override
	public void write(int b) throws IOException {
	}
	
	@Override
	public void close() throws IOException {
		throw new IOException();
	}

}
