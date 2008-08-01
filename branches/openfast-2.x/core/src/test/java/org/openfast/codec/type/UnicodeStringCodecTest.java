package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.codec.StringCodec;
import org.openfast.test.OpenFastTestCase;

public class UnicodeStringCodecTest extends OpenFastTestCase {
    StringCodec codec = new UnicodeStringCodec();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
    public void testDecode() {
        assertEquals(new String("\u03B1\u03B2\u03B3"), decode("10000110 11001110 10110001 11001110 10110010 11001110 10110011"));
        assertEquals(new String("abc"), decode("10000011 01100001 01100010 01100011"));
    }

    private String decode(String bits) {
        return codec.decode(buffer(bits));
    }

    public void testEncode() {
        assertEquals("10000110 11001110 10110001 11001110 10110010 11001110 10110011", encode(new String("\u03B1\u03B2\u03B3")));
        assertEquals("10000011 01100001 01100010 01100011", encode(new String("abc")));
    }

    private ByteBuffer encode(String value) {
        buffer.clear();
        codec.encode(buffer, value);
        return buffer;
    }
}
