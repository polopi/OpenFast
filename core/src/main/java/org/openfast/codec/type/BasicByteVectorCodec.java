package org.openfast.codec.type;

import static org.openfast.codec.type.FastTypeCodecs.UNSIGNED_INTEGER;
import java.nio.ByteBuffer;
import org.openfast.codec.ByteVectorCodec;

public class BasicByteVectorCodec extends LengthEncodedTypeCodec implements ByteVectorCodec {
    public byte[] decode(ByteBuffer buffer) {
        int length = UNSIGNED_INTEGER.decode(buffer);
        byte[] bytes = new byte[length];
        buffer.get(bytes);
        return bytes;
    }

    public void encode(ByteBuffer buffer, byte[] value) {
        UNSIGNED_INTEGER.encode(buffer, value.length);
        buffer.put(value);
    }

    public boolean isNull(ByteBuffer buffer) {
        return false;
    }
}
