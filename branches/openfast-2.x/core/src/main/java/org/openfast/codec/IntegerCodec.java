package org.openfast.codec;

import java.nio.ByteBuffer;

public interface IntegerCodec extends TypeCodec {
    int decode(ByteBuffer buffer);
    
    /**
     * Encodes the value into the FAST encoded buffer
     * 
     * @param buffer the destination for fast encoded bytes
     * @param value the value to encode
     */
    void encode(ByteBuffer buffer, int value);
}
