package org.openfast.codec.type;

import static org.openfast.Fast.STOP_BIT;
import static org.openfast.Fast.VALUE_BITS;
import java.nio.ByteBuffer;
import org.openfast.Global;
import org.openfast.codec.IntegerCodec;
import org.openfast.error.FastConstants;


public class UnsignedIntegerCodec extends StopBitEncodedTypeCodec implements IntegerCodec {
    public int decode(ByteBuffer buffer) {
        if (buffer.get(0) == 0)
            Global.handleError(FastConstants.R6_OVERLONG_INT, "Encountered overlong integer.");
        int value = 0;
        byte byt;
        do {
            byt = buffer.get();
            value = (value << 7) | (byt & VALUE_BITS);
        } while ((byt & STOP_BIT) == 0);
        return value;
    }

    public void encode(ByteBuffer buffer, int value) {
        int size = getUnsignedIntegerSize(value);
        int factor = (size-1) * 7;
        while (factor >= 0) {
            buffer.put((byte) ((value >> (factor)) & VALUE_BITS));
            factor -= 7;
        }
        buffer.array()[buffer.position() - 1] |= STOP_BIT;
    }
    
    /**
     * 
     * @param value
     *            The long to determine the unsigned integer
     * @return Returns an unsigned integer
     */
    public static int getUnsignedIntegerSize(int value) {
        if (value < 128) {
            return 1; // 2 ^ 7
        }
        if (value <= 16384) {
            return 2; // 2 ^ 14
        }
        if (value <= 2097152) {
            return 3; // 2 ^ 21
        }
        if (value <= 268435456) {
            return 4; // 2 ^ 28
        }
        return 5;
    }

    public boolean isNull(ByteBuffer buffer) {
        return false;
    }
}
