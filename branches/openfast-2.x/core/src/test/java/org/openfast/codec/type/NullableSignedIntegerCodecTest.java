package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Global;
import org.openfast.codec.IntegerCodec;
import org.openfast.error.ErrorHandler;
import org.openfast.error.FastConstants;
import org.openfast.error.FastException;
import org.openfast.test.OpenFastTestCase;

public class NullableSignedIntegerCodecTest extends OpenFastTestCase {
    IntegerCodec codec = new NullableSignedIntegerCodec();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
    public void testOverlongInteger() {
        try {
            decode("00000000 10000001");
            fail();
        } catch (FastException e) {
            assertEquals(FastConstants.R6_OVERLONG_INT, e.getCode());
        }
    }
    
    public void testOverlongIgnored() {
        Global.setErrorHandler(ErrorHandler.NULL);
        assertEquals(1, decode("00000000 10000010"));
        Global.setErrorHandler(ErrorHandler.DEFAULT);
    }
    
    public void testBoundaries() {
        assertEquals("01111000 00000000 00000000 00000000 10000000", encode(Integer.MIN_VALUE));
        assertEquals("00001000 00000000 00000000 00000000 10000000", encode(Integer.MAX_VALUE));
    }
    
    public void testDecode() {
        assertEquals(62, codec.decode(buffer("10111111")));
        assertEquals(63, codec.decode(buffer("00000000 11000000")));
        assertEquals(-1, codec.decode(buffer("11111111")));
        assertEquals(-2, codec.decode(buffer("11111110")));
        assertEquals(-64, codec.decode(buffer("11000000")));
        assertEquals(-65, codec.decode(buffer("01111111 10111111")));
        assertEquals(638, codec.decode(buffer("00000100 11111111")));
        assertEquals(942754, codec.decode(buffer("00111001 01000101 10100011")));
        assertEquals(-942755, codec.decode(buffer("01000110 00111010 11011101")));
        assertEquals(8192, codec.decode(buffer("00000000 01000000 10000001")));
        assertEquals(-8193, codec.decode(buffer("01111111 00111111 11111111")));
    }
    
    public void testEncode() {
        assertEncode(buffer, "10111111"                  , 62);
        assertEncode(buffer, "00000000 11000000"         , 63);
        assertEncode(buffer, "11111111"                  , -1);
        assertEncode(buffer, "11111110"                  , -2);
        assertEncode(buffer, "11000000"                  , -64);
        assertEncode(buffer, "01111111 10111111"         , -65);
        assertEncode(buffer, "00000100 11111111"         , 638);
        assertEncode(buffer, "00111001 01000101 10100011", 942754);
        assertEncode(buffer, "01000110 00111010 11011101", -942755);
        assertEncode(buffer, "00000000 01000000 10000001", 8192);
        assertEncode(buffer, "01111111 00111111 11111111", -8193);
    }

    private void assertEncode(ByteBuffer buffer, String bitstring, int value) {
        buffer.clear();
        codec.encode(buffer, value);
        assertEquals(bitstring, buffer);
    }

    public void testGetLength() {
        assertEquals(1, codec.getLength(buffer("10000000 01010101 10000000")));
        assertEquals(2, codec.getLength(buffer("00100000 10000000 00001111")));
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
