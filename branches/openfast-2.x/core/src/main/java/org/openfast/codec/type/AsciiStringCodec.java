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

public class AsciiStringCodec extends StopBitEncodedTypeCodec implements StringCodec {
    private final CharsetDecoder decoder = Charset.forName("US-ASCII").newDecoder();
    private final CharsetEncoder encoder = Charset.forName("US-ASCII").newEncoder();
    public String decode(byte[] buffer, int offset) {
        CharBuffer decoded;
        try {
            int length = getLength(buffer, offset);
            buffer[length + offset - 1] &= Fast.VALUE_BITS; // remove stop bit
            if (buffer[offset] == 0) {
                if (!ByteUtil.isEmpty(buffer, offset, length))
                    Global.handleError(FastConstants.R9_STRING_OVERLONG, null);
                if (length > 1 && buffer[offset+1] == 0)
                    return Fast.ZERO_TERMINATOR;
                return "";
            }
            decoded = decoder.decode(ByteBuffer.wrap(buffer, offset, length));
            buffer[length + offset - 1] |= Fast.STOP_BIT; // replace stop bit to prevent side effects
            return decoded.toString();
        } catch (CharacterCodingException e) {
            throw new RuntimeException(e);
        }
    }

    public int encode(byte[] buffer, int offset, String value) {
        if (value.length() == 0) {
            buffer[offset] = Fast.NULL;
            return offset + 1;
        }
        if (value.startsWith(Fast.ZERO_TERMINATOR)) {
            buffer[offset] = 0;
            buffer[offset+1] = Fast.STOP_BIT;
            return offset + 2;
        }
        ByteBuffer encoded;
        try {
            encoded = encoder.encode(CharBuffer.wrap(value));
        } catch (CharacterCodingException e) {
            throw new RuntimeException(e);
        }
        encoded.get(buffer, offset, encoded.limit());
        buffer[encoded.limit() - 1 + offset] |= Fast.STOP_BIT;
        return encoded.limit() + offset;
    }

    public boolean isNull(byte[] buffer, int offset) {
        return false;
    }
}