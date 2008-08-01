package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.codec.LongCodec;

public class NullableSignedLongCodec extends StopBitEncodedTypeCodec implements LongCodec {
    public long decode(ByteBuffer buffer) {
        int value = FastTypeCodecs.SIGNED_INTEGER.decode(buffer);
        if (value > 0)
            return value - 1;
        return value;
    }

    public void encode(ByteBuffer buffer, long value) {
        // handle maximum value case due to nullable max value being larger than long capacity
        if (value == Long.MAX_VALUE) {
            buffer.put((byte) 1);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put(Fast.STOP_BIT);
            return;
        }
        if (value >= 0)
            FastTypeCodecs.SIGNED_LONG.encode(buffer, value + 1);
        else
            FastTypeCodecs.SIGNED_LONG.encode(buffer, value);
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}
