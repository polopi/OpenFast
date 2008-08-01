package org.openfast.codec;

import java.nio.ByteBuffer;


public interface ByteVectorCodec extends TypeCodec {
    byte[] decode(ByteBuffer buffer);
    void encode(ByteBuffer buffer, byte[] bytes);
}
