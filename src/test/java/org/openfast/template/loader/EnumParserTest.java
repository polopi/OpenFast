package org.openfast.template.loader;

import org.openfast.Dictionary;
import org.openfast.ScalarValue;
import org.openfast.template.Define;
import org.openfast.template.Scalar;
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
		Scalar uint = (Scalar) parser.parse(enumString, context);
		assertScalarField(uint, Type.ENUM, "someEnum", null, "", Dictionary.GLOBAL, "someEnum", Operator.COPY, ScalarValue.UNDEFINED, false);
	}
}
