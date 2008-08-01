package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.codec.IntegerCodec;

public class NullableUnsignedIntegerCodec extends StopBitEncodedTypeCodec implements IntegerCodec {
    public int decode(ByteBuffer buffer) {
        return FastTypeCodecs.UNSIGNED_INTEGER.decode(buffer) - 1;
    }

    public void encode(ByteBuffer buffer, int value) {
        if (value == Integer.MAX_VALUE) {
            buffer.put((byte) 8);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put(Fast.STOP_BIT);
            return;
        }
        FastTypeCodecs.UNSIGNED_INTEGER.encode(buffer, value + 1);
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}
