package org.openfast.codec;

import java.nio.ByteBuffer;
import org.openfast.ULong;

public interface ULongCodec extends TypeCodec {
    ULong decode(ByteBuffer buffer);
    void encode(ByteBuffer buffer, ULong value);
}
