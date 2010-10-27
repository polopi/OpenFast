package org.openfast.codec.type;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.nio.charset.CharsetEncoder;
import org.openfast.ByteUtil;
import org.openfast.Fast;
import org.openfast.Global;
import org.openfast.codec.StringCodec;
import org.openfast.error.FastConstants;

public class NullableAsciiStringCodec extends StopBitEncodedTypeCodec implements StringCodec {
    private final CharsetDecoder decoder = Charset.forName("US-ASCII").newDecoder();
    private final CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
    public String decode(ByteBuffer bbuf) {
        CharBuffer decoded;
        try {
            int length = getLength(bbuf);
            byte[] buffer = new byte[length];
            bbuf.get(buffer);
            if ((buffer[0] & Fast.VALUE_BITS) == 0) {
                if (!ByteUtil.isEmpty(buffer, 0, length))
                    Global.handleError(FastConstants.R9_STRING_OVERLONG, null);
                if (length == 3 && buffer[1] == 0 && (buffer[2] & Fast.VALUE_BITS) == 0)
                    return Fast.ZERO_TERMINATOR;
                if (length == 2 && (buffer[1] & Fast.VALUE_BITS) == 0)
                    return "";
                return null;
            }
            buffer[length - 1] &= Fast.VALUE_BITS; // remove stop bit
            decoded = decoder.decode(ByteBuffer.wrap(buffer));
            buffer[length - 1] |= Fast.STOP_BIT; // replace stop bit to prevent side effects
            return decoded.toString();
        } catch (CharacterCodingException e) {
            throw new RuntimeException(e);
        }
    }

    public void encode(ByteBuffer buffer, String value) {
        if (value == null) {
            buffer.put(Fast.NULL);
            return;
        }
        if (value.length() == 0) {
            buffer.put((byte) 0);
            buffer.put(Fast.STOP_BIT);
            return;
        }
        if (value.startsWith(Fast.ZERO_TERMINATOR)) {
            buffer.put((byte) 0);
            buffer.put((byte) 0);
            buffer.put(Fast.STOP_BIT);
            return;
        }
        encoder.encode(CharBuffer.wrap(value), buffer, true);
        buffer.array()[buffer.position()-1] |= Fast.STOP_BIT;
    }

    public boolean isNull(ByteBuffer buffer) {
        return buffer.get(0) == Fast.NULL;
    }
}