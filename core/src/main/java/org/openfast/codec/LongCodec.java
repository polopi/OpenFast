package org.openfast.codec;

import java.nio.ByteBuffer;

public interface LongCodec extends TypeCodec {
    long decode(ByteBuffer buffer);
    void encode(ByteBuffer buffer, long value);
}
