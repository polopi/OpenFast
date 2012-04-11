package org.openfast.codec.type;

import org.openfast.Global;
import org.openfast.codec.IntegerCodec;
import org.openfast.error.ErrorHandler;
import org.openfast.error.FastConstants;
import org.openfast.error.FastException;
import org.openfast.test.OpenFastTestCase;

public class UnsignedIntegerCodecTest extends OpenFastTestCase {
    IntegerCodec codec = new UnsignedIntegerCodec();
    private byte[] buffer = new byte[10];
    
    public void testBoundaries() {
        assertEquals(Integer.MAX_VALUE, decode("00000111 01111111 01111111 01111111 11111111"));
        assertEquals("00000111 01111111 01111111 01111111 11111111", encode(Integer.MAX_VALUE), 5);
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
        assertEquals("10000001", encode(1), 1);
        assertEquals("10000010", encode(2), 1);
    }

    private int decode(String bits) {
        return codec.decode(buffer(bits));
    }

    private byte[] encode(int value) {
        codec.encode(buffer, 0, value);
        return buffer;
    }
}