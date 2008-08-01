package org.openfast.codec;

import java.nio.ByteBuffer;
import org.openfast.Decimal;

public interface DecimalCodec extends TypeCodec {
    Decimal decode(ByteBuffer buffer);
    void encode(ByteBuffer buffer, Decimal value);
}
