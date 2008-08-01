package org.openfast.codec;

import java.nio.ByteBuffer;

public interface StringCodec extends TypeCodec {
    String decode(ByteBuffer buffer);
    void encode(ByteBuffer buffer, String value);
}
