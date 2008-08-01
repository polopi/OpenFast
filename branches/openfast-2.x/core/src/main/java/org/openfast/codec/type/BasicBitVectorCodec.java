package org.openfast.codec.type;

import static org.openfast.Fast.STOP_BIT;
import static org.openfast.Fast.VALUE_BITS;
import java.nio.ByteBuffer;
import org.openfast.codec.BitVectorCodec;
import org.openfast.util.BitVector;

public class BasicBitVectorCodec extends StopBitEncodedTypeCodec implements BitVectorCodec {
    public BitVector decode(ByteBuffer buffer) {
        int len = getLength(buffer);
        byte[] newBuffer = new byte[len];
        buffer.get(newBuffer);
        return new BitVector(newBuffer);
    }

    public void encode(ByteBuffer buffer, BitVector vector) {
        byte[] bytes = vector.getBytes();
        int index = bytes.length - 1;
        while (index > 0 && (bytes[index] & VALUE_BITS) == 0)
            index--;
        buffer.put(bytes, 0, index + 1);
        buffer.array()[buffer.position()-1] |= STOP_BIT;
    }

    public boolean isNull(ByteBuffer buffer) {
        return false;
    }
}
