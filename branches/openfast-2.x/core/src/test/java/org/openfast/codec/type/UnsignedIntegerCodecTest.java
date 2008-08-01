package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Global;
import org.openfast.codec.IntegerCodec;
import org.openfast.error.ErrorHandler;
import org.openfast.error.FastConstants;
import org.openfast.error.FastException;
import org.openfast.test.OpenFastTestCase;

public class UnsignedIntegerCodecTest extends OpenFastTestCase {
    IntegerCodec codec = new UnsignedIntegerCodec();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
    public void testBoundaries() {
        assertEquals(Integer.MAX_VALUE, decode("00000111 01111111 01111111 01111111 11111111"));
        assertEquals("00000111 01111111 01111111 01111111 11111111", encode(Integer.MAX_VALUE));
    }
    
    public void testOverlong() {
        try {
            decode("00000000 11000000");
            fail();
        } catch (FastException e) {
            assertEquals(FastConstants.R6_OVERLONG_INT, e.getCode());
        }
    }
    
    public void testOverlongIgnored() {
        Global.setErrorHandler(ErrorHandler.NULL);
        assertEquals(64, decode("00000000 11000000"));
        Global.setErrorHandler(ErrorHandler.DEFAULT);
    }
    
    public void testDecode() {
        assertEquals(1, decode("10000001"));
        assertEquals(2, decode("10000010"));
    }

    public void testEncode() {
        assertEquals("10000001", encode(1));
        assertEquals("10000010", encode(2));
    }

    private int decode(String bits) {
        return codec.decode(buffer(bits));
    }

    private ByteBuffer encode(int value) {
        buffer.clear();
        codec.encode(buffer, value);
        return buffer;
    }
}
