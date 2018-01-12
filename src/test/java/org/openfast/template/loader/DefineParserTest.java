package org.openfast.template.loader;

import org.openfast.template.Define;
import org.openfast.template.operator.Operator;
import org.openfast.template.type.Type;
import org.openfast.test.OpenFastTestCase;
import org.w3c.dom.Element;

public class DefineParserTest extends OpenFastTestCase {

	private DefineParser parser;
	private ParsingContext context;

	protected void setUp() throws Exception {
		parser = new DefineParser();
		context = XMLMessageTemplateLoader.createInitialContext();
	}
	
	public void testParseDefineString() throws Exception {
		Element defineString = document("<define name=\"value\"><string/></define>").getDocumentElement();
		assertTrue(parser.canParse(defineString, context));
		Define define = (Define) parser.parse(defineString, context);
		assertScalarField(define,0,  Type.ASCII, null, Operator.NONE);
	}

	public void testParseDefineStringWithCopy() throws Exception {
		Element defineString = document("<define name=\"value\"><string><copy/></string></define>").getDocumentElement();
		assertTrue(parser.canParse(defineString, context));
		Define define = (Define) parser.parse(defineString, context);
		assertScalarField(define,0,  Type.ASCII, null, Operator.COPY);
	}
}
