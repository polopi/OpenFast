package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Global;
import org.openfast.codec.LongCodec;
import org.openfast.error.ErrorHandler;
import org.openfast.error.FastConstants;
import org.openfast.error.FastException;
import org.openfast.test.OpenFastTestCase;

public class SignedLongCodecTest extends OpenFastTestCase {
    LongCodec codec = new SignedLongCodec();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    public void testOverlong() {
        try {
            decode("00000000 10000001");
            fail();
        } catch (FastException e) {
            assertEquals(FastConstants.R6_OVERLONG_INT, e.getCode());
        }
    }
    
    public void testOverlongIgnored() {
        Global.setErrorHandler(ErrorHandler.NULL);
        assertEquals(1, decode("00000000 10000001"));
        Global.setErrorHandler(ErrorHandler.DEFAULT);
    }
    
    public void testBoundaries() {
        assertEquals("01111111 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 10000000", encode(Long.MIN_VALUE));
        assertEquals("00000000 01111111 01111111 01111111 01111111 01111111 01111111 01111111 01111111 11111111", encode(Long.MAX_VALUE));
    }
    
    public void testDecode() {
        assertEquals(63, codec.decode(buffer("10111111")));
        assertEquals(64, codec.decode(buffer("00000000 11000000")));
        assertEquals(-1, codec.decode(buffer("11111111")));
        assertEquals(-2, codec.decode(buffer("11111110")));
        assertEquals(-64, codec.decode(buffer("11000000")));
        assertEquals(-65, codec.decode(buffer("01111111 10111111")));
        assertEquals(639, codec.decode(buffer("00000100 11111111")));
        assertEquals(942755, codec.decode(buffer("00111001 01000101 10100011")));
        assertEquals(-942755, codec.decode(buffer("01000110 00111010 11011101")));
        assertEquals(8193, codec.decode(buffer("00000000 01000000 10000001")));
        assertEquals(-8193, codec.decode(buffer("01111111 00111111 11111111")));
    }
    
    public void testEncode() {
        assertEncode(buffer, "10111111"                  , 63);
        assertEncode(buffer, "00000000 11000000"         , 64);
        assertEncode(buffer, "11111111"                  , -1);
        assertEncode(buffer, "11111110"                  , -2);
        assertEncode(buffer, "11000000"                  , -64);
        assertEncode(buffer, "01111111 10111111"         , -65);
        assertEncode(buffer, "00000100 11111111"         , 639);
        assertEncode(buffer, "00111001 01000101 10100011", 942755);
        assertEncode(buffer, "01000110 00111010 11011101", -942755);
        assertEncode(buffer, "00000000 01000000 10000001", 8193);
        assertEncode(buffer, "01111111 00111111 11111111", -8193);
    }

    private void assertEncode(ByteBuffer buffer, String bitstring, int value) {
        codec.encode(buffer, value);
        assertEquals(bitstring, buffer);
        buffer.clear();
    }

    public void testGetLength() {
        assertEquals(1, codec.getLength(buffer("10000000 01010101 10000000")));
        assertEquals(2, codec.getLength(buffer("00100000 10000000 00001111")));
    }

    private long decode(String bits) {
        return codec.decode(buffer(bits));
    }

    private ByteBuffer encode(long value) {
        buffer.clear();
        codec.encode(buffer, value);
        return buffer;
    }
}
