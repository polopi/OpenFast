package org.openfast.codec;

import java.nio.ByteBuffer;
import org.openfast.util.BitVector;

public interface BitVectorCodec extends TypeCodec {
    BitVector decode(ByteBuffer buffer);
    void encode(ByteBuffer buffer, BitVector vector);
}
