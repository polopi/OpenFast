package org.openfast;

import org.openfast.template.MessageTemplate;
import org.openfast.test.OpenFastTestCase;

public class TypeConversionTest extends OpenFastTestCase {
	
	public void testConversions() {
		MessageTemplate template = template(
				"<template>" +
				"  <string name=\"string\"/>" +
				"  <u32 name=\"uint\"/>" +
				"  <i8 name=\"byte\"/>" +
				"  <i16 name=\"short\"/>" +
				"  <i64 name=\"long\"/>" +
				"  <byte name=\"bytevector\"/>" +
				"  <decimal name=\"decimal\"/>" +
				"</template>");
		
		Message message = new Message(template, 102);
		message.setByteVector("string", byt("7f001a"));
		message.setDecimal("uint", 150.0);
		message.setString("byte", "4");
		message.setString("short", "-5");
		message.setString("long", "1000000000000000000");
		message.setString("bytevector", "abcd");
		
		byte[] encoding = template.encode(message, new Context());
		GroupValue decodedMessage = (GroupValue) template.decode(stream(encoding), template, new Context(), true);
		
		assertEquals("7f001a", decodedMessage.getString("string"));
		assertEquals(150, decodedMessage.getInt("uint"));
		assertEquals(150, decodedMessage.getShort("uint"));
		assertEquals(4, decodedMessage.getByte("byte"));
		assertEquals(-5, decodedMessage.getShort("short"));
		assertEquals(1000000000000000000L, decodedMessage.getLong("long"));
		assertEquals("61626364", decodedMessage.getString("bytevector"));
	}
}
