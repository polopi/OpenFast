package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.codec.LongCodec;

public class NullableUnsignedLongCodec extends StopBitEncodedTypeCodec implements LongCodec {
    public long decode(ByteBuffer buffer) {
        return FastTypeCodecs.UNSIGNED_LONG.decode(buffer) - 1;
    }

    public void encode(ByteBuffer buffer, long value) {
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
            buffer.put((byte) Fast.STOP_BIT);
            return;
        }
        FastTypeCodecs.UNSIGNED_LONG.encode(buffer, value + 1);
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}
