package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.Global;
import org.openfast.ULong;
import org.openfast.codec.ULongCodec;
import org.openfast.error.ErrorHandler;
import org.openfast.error.FastConstants;
import org.openfast.error.FastException;
import org.openfast.test.OpenFastTestCase;

public class NullableULongCodecTest extends OpenFastTestCase {
    ULongCodec codec = new BasicULongCodec();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
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
        assertEquals(new ULong(64), decode("00000000 11000000"));
        Global.setErrorHandler(ErrorHandler.DEFAULT);
    }
    
    public void testDecode() {
        assertEquals(new ULong(0x7fffffffffffffffL), decode("01111111 01111111 01111111 01111111 01111111 01111111 01111111 01111111 11111111"));
        assertEquals(new ULong(0x8000000000000000L), decode("00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 10000000"));
        assertEquals(new ULong(0xffffffffffffffffL), decode("00000001 01111111 01111111 01111111 01111111 01111111 01111111 01111111 01111111 11111111"));
    }


    public void testEncode() {
        assertEquals("01111111 01111111 01111111 01111111 01111111 01111111 01111111 01111111 11111111", encode(new ULong(0x7fffffffffffffffL)));
        assertEquals("00000001 00000000 00000000 00000000 00000000 00000000 00000000 00000000 00000000 10000000", encode(new ULong(0x8000000000000000L)));
        assertEquals("00000001 01111111 01111111 01111111 01111111 01111111 01111111 01111111 01111111 11111111", encode(new ULong(0xffffffffffffffffL)));
    }
    
    private ByteBuffer encode(ULong value) {
        buffer.clear();
        codec.encode(buffer, value);
        return buffer;
    }


    private ULong decode(String bits) {
        return codec.decode(buffer(bits));
    }
}
