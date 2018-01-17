package org.openfast.template.loader;

import org.openfast.Dictionary;
import org.openfast.ScalarValue;
import org.openfast.template.Define;
import org.openfast.template.Field;
import org.openfast.template.Scalar;
import org.openfast.template.operator.Operator;
import org.openfast.template.type.Type;
import org.openfast.test.OpenFastTestCase;
import org.w3c.dom.Element;

public class FieldParserTest extends OpenFastTestCase {

    private FieldParser parser;
    private ParsingContext context;

    protected void setUp() throws Exception {
        parser = new FieldParser();
        context = XMLMessageTemplateLoader.createInitialContext();
    }

    public void testParseFieldString() throws Exception {
        context.getDefineRegistry().define(new Define("customString", new Field[] {new Scalar("",Type.ASCII,Operator.NONE,null,false)}) );
        Element fieldString = document("<field name=\"text\"><type name=\"customString\"/></field>").getDocumentElement();
        assertTrue(parser.canParse(fieldString, context));
        Scalar string = (Scalar) parser.parse(fieldString, context);
        assertScalarField(string, Type.ASCII, "text", null, "", Dictionary.GLOBAL, "text", Operator.NONE,
            ScalarValue.UNDEFINED, false);
    }

    public void testParseFieldStringWithoutDefine() throws Exception {
        Element fieldString = document("<field name=\"text\" id=\"123\"><string><copy/></string></field>").getDocumentElement();
        assertTrue(parser.canParse(fieldString, context));
        Scalar string = (Scalar) parser.parse(fieldString, context);
        assertScalarField(string, Type.ASCII, "text", "123", "", Dictionary.GLOBAL, "text", Operator.COPY,
            ScalarValue.UNDEFINED, false);
    }

    public void testParseFieldStringWithCopy() throws Exception {
        context.getDefineRegistry().define(new Define("customString", new Field[] {new Scalar("",Type.ASCII,Operator.COPY,null,false)}) );
        Element defineString = document("<field name=\"text\"><type name=\"customString\"/></field>").getDocumentElement();
        assertTrue(parser.canParse(defineString, context));
        Scalar string = (Scalar) parser.parse(defineString, context);
        assertScalarField(string, Type.ASCII, "text", null, "", Dictionary.GLOBAL, "text", Operator.COPY,
            ScalarValue.UNDEFINED, false);
    }
}