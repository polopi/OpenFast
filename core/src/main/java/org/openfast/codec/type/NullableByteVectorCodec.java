package org.openfast.codec.type;

import static org.openfast.codec.type.FastTypeCodecs.NULLABLE_UNSIGNED_INTEGER;
import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.codec.ByteVectorCodec;

public class NullableByteVectorCodec extends LengthEncodedTypeCodec implements ByteVectorCodec {
    public byte[] decode(ByteBuffer buffer) {
        int length = NULLABLE_UNSIGNED_INTEGER.decode(buffer);
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public void encode(ByteBuffer buffer, byte[] value) {
        NULLABLE_UNSIGNED_INTEGER.encode(buffer, value.length);
        buffer.put(value);
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}
