package org.openfast.ext.mina;

import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.SimpleByteBufferAllocator;

public class MinaFastServer {
	public static void main(String[] args) {
		ByteBuffer.setUseDirectBuffers(false);
		ByteBuffer.setAllocator(new SimpleByteBufferAllocator());
		
//		IoAcceptor acceptor = new SocketAcceptor();
	}
}
