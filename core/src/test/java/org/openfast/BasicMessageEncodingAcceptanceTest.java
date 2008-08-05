package org.openfast;

import java.nio.ByteBuffer;
import org.openfast.codec.FastEncoder;
import org.openfast.template.MessageTemplate;
import org.openfast.template.loader.XMLMessageTemplateLoader;
import org.openfast.test.OpenFastTestCase;

public class BasicMessageEncodingAcceptanceTest extends OpenFastTestCase {
    public void testBasicFastImplementation() throws Exception {
        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader();
        loader.setLoadTemplateIdFromAuxId(true);
        loader.load(resource("acceptance/integerTemplates.xml"));
        MessageTemplate template = loader.getTemplateRegistry().get("incrementInteger");
        
        FastEncoder encoder = new FastEncoder(loader.getTemplateRegistry());
        ByteBuffer buffer = ByteBuffer.allocate(256);
        Message message = template.newMessage();
        message.set(0, 24);
        encoder.encode(buffer, message);
        assertEquals("11100000 10000001 10011000", buffer);
        
        buffer.clear();
        message.set(0, 25);
        encoder.encode(buffer, message);
        assertEquals("10000000", buffer);
    }
}
