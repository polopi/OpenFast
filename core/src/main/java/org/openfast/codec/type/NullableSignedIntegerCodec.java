package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.codec.IntegerCodec;

public class NullableSignedIntegerCodec extends StopBitEncodedTypeCodec implements IntegerCodec {
    public int decode(ByteBuffer buffer) {
        int value = FastTypeCodecs.SIGNED_INTEGER.decode(buffer);
        if (value > 0)
            return value - 1;
        return value;
    }

    public void encode(ByteBuffer buffer, int value) {
        // handle maximum value case due to nullable max value being larger than int capacity
        if (value == Integer.MAX_VALUE) {
            buffer.put((byte) 8);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put(Fast.STOP_BIT);
            return;
        }
        if (value >= 0)
            FastTypeCodecs.SIGNED_INTEGER.encode(buffer, value + 1);
        else
            FastTypeCodecs.SIGNED_INTEGER.encode(buffer, value);
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}
