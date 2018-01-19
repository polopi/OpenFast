package org.openfast.template.loader;

import org.openfast.Dictionary;
import org.openfast.ScalarValue;
import org.openfast.template.EnumScalar;
import org.openfast.template.operator.Operator;
import org.openfast.template.type.Type;
import org.openfast.test.OpenFastTestCase;
import org.w3c.dom.Element;

public class EnumParserTest extends OpenFastTestCase {

	private EnumParser parser;
	private ParsingContext context;

	protected void setUp() throws Exception {
		parser = new EnumParser();
		context = XMLMessageTemplateLoader.createInitialContext();
	}
	
	public void testParseDefineString() throws Exception {
		Element enumString = document("<enum name=\"someEnum\"><element name=\"ONE\"/><element name=\"TWO\"/><copy/></enum>").getDocumentElement();
		assertTrue(parser.canParse(enumString, context));
		EnumScalar enumScalar = (EnumScalar) parser.parse(enumString, context);
		assertScalarField(enumScalar, Type.ENUM, "someEnum", null, "", Dictionary.GLOBAL, "someEnum", Operator.COPY, ScalarValue.UNDEFINED, false);
		assertEquals(2, enumScalar.getEnumValues().length);
	}
}
