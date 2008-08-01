package org.openfast.codec.type;

import static org.openfast.Fast.SIGN_BIT;
import static org.openfast.Fast.STOP_BIT;
import static org.openfast.Fast.VALUE_BITS;
import java.nio.ByteBuffer;
import org.openfast.Fast;
import org.openfast.Global;
import org.openfast.codec.LongCodec;
import org.openfast.error.FastConstants;

public class SignedLongCodec extends StopBitEncodedTypeCodec implements LongCodec {
    public long decode(ByteBuffer buffer) {
        long value = 0;
        long byt = buffer.get();
        if (byt == 0 && (buffer.get(buffer.position()) & SIGN_BIT) == 0) {
            Global.handleError(FastConstants.R6_OVERLONG_INT, "Encountered overlong integer.");
        }
        if ((byt & SIGN_BIT) > 0) {
            value = -1;
        }
        value = (value << 7) | (byt & VALUE_BITS);
        while ((byt & STOP_BIT) == 0) {
            byt = buffer.get();
            value = (value << 7) | (byt & VALUE_BITS);
        }
        return value;
    }

    public void encode(ByteBuffer buffer, long value) {
        int index = buffer.position();
        int size = getSignedLongSize(value);
        int factor = (size-1) * 7;
        int bitMask = 0x3f;
        while (factor >= 0) {
            buffer.put((byte)((value >> factor) & bitMask));
            bitMask = 0x7f;
            factor -= 7;
        }
        // Get the sign bit from the value and set it on the first byte
        // 01000000 00000000 ... 00000000
        // ^----SIGN BIT
        buffer.array()[index] |= (0x40 & (value >> 57));
        buffer.array()[buffer.position()-1] |= Fast.STOP_BIT;
    }
    
    /**
     * Find the signed integer size for the passed long value
     * 
     * @param value
     *            The long value to be used to get the signed integer size
     * @return Returns an integer of the appropriate signed integer
     */
    public static int getSignedLongSize(long value) {
        if ((value >= -64) && (value <= 63)) {
            return 1; // - 2 ^ 6 ... 2 ^ 6 -1
        }
        if ((value >= -8192) && (value <= 8191)) {
            return 2; // - 2 ^ 13 ... 2 ^ 13 -1
        }
        if ((value >= -1048576) && (value <= 1048575)) {
            return 3; // - 2 ^ 20 ... 2 ^ 20 -1
        }
        if ((value >= -134217728) && (value <= 134217727)) {
            return 4; // - 2 ^ 27 ... 2 ^ 27 -1
        }
        if ((value >= -17179869184L) && (value <= 17179869183L)) {
            return 5; // - 2 ^ 34 ... 2 ^ 34 -1
        }
        if ((value >= -2199023255552L) && (value <= 2199023255551L)) {
            return 6; // - 2 ^ 41 ... 2 ^ 41 -1
        }
        if ((value >= -281474976710656L) && (value <= 281474976710655L)) {
            return 7; // - 2 ^ 48 ... 2 ^ 48 -1
        }
        if ((value >= -36028797018963968L) && (value <= 36028797018963967L)) {
            return 8; // - 2 ^ 55 ... 2 ^ 55 -1
        }
        if ((value >= -4611686018427387904L && value <= 4611686018427387903L)) {
            return 9;
        }
        return 10;
    }

    public boolean isNull(ByteBuffer buffer) {
        return false;
    }
}
