package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.ULong;
import org.openfast.codec.ULongCodec;

public class NullableULongCodec extends StopBitEncodedTypeCodec implements ULongCodec {
    public ULong decode(ByteBuffer buffer) {
        long value = FastTypeCodecs.NULLABLE_UNSIGNED_LONG.decode(buffer);
        return new ULong(value);
    }

    public void encode(ByteBuffer buffer, ULong value) {
        if (value.longValue() == ULong.MAX_VALUE) {
            buffer.put((byte) 2);
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
        FastTypeCodecs.NULLABLE_UNSIGNED_LONG.encode(buffer, value.longValue());
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}
