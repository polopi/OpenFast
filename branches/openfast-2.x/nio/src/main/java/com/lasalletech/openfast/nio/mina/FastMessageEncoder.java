package com.lasalletech.openfast.nio.mina;

import java.util.Collections;
import java.util.Set;
import org.apache.mina.common.ByteBuffer;
import org.apache.mina.common.IoSession;
import org.apache.mina.filter.codec.ProtocolEncoderOutput;
import org.apache.mina.filter.codec.demux.MessageEncoder;
import org.openfast.Message;
import org.openfast.codec.FastEncoder;

public class FastMessageEncoder implements MessageEncoder {
    public static final String ENCODER = "ENCODER";

    public FastMessageEncoder() {
    }

    public void encode(IoSession session, Object message, ProtocolEncoderOutput out) throws Exception {
        FastEncoder encoder = (FastEncoder) session.getAttribute(ENCODER);
        java.nio.ByteBuffer buffer = java.nio.ByteBuffer.allocateDirect(1024*32); // PERFORMANCE - Cache buffer
        encoder.encode(buffer, (Message) message);
        out.write(ByteBuffer.wrap(buffer));
    }

    @SuppressWarnings("unchecked")
    public Set getMessageTypes() {
        return Collections.singleton(Message.class);
    }

}