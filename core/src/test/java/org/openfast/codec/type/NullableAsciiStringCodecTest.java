package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.codec.StringCodec;
import org.openfast.test.OpenFastTestCase;

public class NullableAsciiStringCodecTest extends OpenFastTestCase {
    StringCodec ascii = new NullableAsciiStringCodec();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
    public void testDecode() {
        assertEquals("ABCD", decode("01000001 01000010 01000011 11000100"));
        assertNull(decode("10000000"));
        assertEquals("\u0000", decode("00000000 00000000 10000000"));
        assertEquals("", decode("00000000 10000000"));
    }

    public void testEncode() {
        assertEquals("01000001 01000010 01000011 11000100", encode("ABCD"));
        assertEquals("10000000", encode(null));
        assertEquals("00000000 00000000 10000000", encode("\u0000"));
        assertEquals("00000000 10000000", encode(""));
    }
    private ByteBuffer encode(String value) {
        buffer.clear();
        ascii.encode(buffer, value);
        return buffer;
    }
    private String decode(String bits) {
        return ascii.decode(buffer(bits));
    }
}
