package org.openfast.codec.type;

import java.nio.ByteBuffer;
import org.openfast.codec.ByteVectorCodec;
import org.openfast.test.OpenFastTestCase;

public class NullableByteVectorCodecTest extends OpenFastTestCase {
    ByteVectorCodec codec = new NullableByteVectorCodec();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
    public void testDecode() {
        assertEquals(new byte[] { (byte) 0xff, 0x3f, 0x0f, 0x03 }, decode("10000101 11111111 00111111 00001111 00000011"));
        assertEquals(new byte[] { (byte) 0x88, 0x66, 0x44, 0x22, 0x00 }, decode("10000110 10001000 01100110 01000100 00100010 00000000"));
    }

    private byte[] decode(String bits) {
        return codec.decode(buffer(bits));
    }

    public void testEncode() {
        assertEquals("10000101 11111111 00111111 00001111 00000011", encode(new byte[] { (byte) 0xff, 0x3f, 0x0f, 0x03 }));
        assertEquals("10000110 10001000 01100110 01000100 00100010 00000000", encode(new byte[] { (byte) 0x88, 0x66, 0x44, 0x22, 0x00 }));
    }

    private ByteBuffer encode(byte[] value) {
        buffer.clear();
        codec.encode(buffer, value);
        return buffer;
    }
}
