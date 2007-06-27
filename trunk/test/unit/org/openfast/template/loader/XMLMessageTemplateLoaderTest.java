/*
The contents of this file are subject to the Mozilla Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is OpenFAST.

The Initial Developer of the Original Code is The LaSalle Technology
Group, LLC.  Portions created by The LaSalle Technology Group, LLC
are Copyright (C) The LaSalle Technology Group, LLC. All Rights Reserved.

Contributor(s): Jacob Northey <jacob@lasalletech.com>
                Craig Otis <cotis@lasalletech.com>
*/


package org.openfast.template.loader;

import junit.framework.TestCase;

import org.openfast.IntegerValue;
import org.openfast.ScalarValue;

import org.openfast.template.FieldSet;
import org.openfast.template.Group;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;
import org.openfast.template.TwinValue;
import org.openfast.template.operator.Operator;
import org.openfast.template.operator.TwinOperator;
import org.openfast.template.type.Type;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class XMLMessageTemplateLoaderTest extends TestCase {
    public void testLoadTemplateThatContainsDecimalWithTwinOperators() {
        String templateXml = "<templates xmlns=\"http://www.fixprotocol.org/ns/template-definition\"" +
            "	ns=\"http://www.fixprotocol.org/ns/templates/sample\">" +
            "	<template name=\"SampleTemplate\">" +
            "		<decimal name=\"bid\"><mantissa><delta /></mantissa><exponent><copy value=\"-2\" /></exponent></decimal>" +
            "		<decimal name=\"ask\"><mantissa><delta /></mantissa></decimal>" +
            "		<decimal name=\"high\"><exponent><copy/></exponent></decimal>" +
            "		<decimal name=\"low\"><mantissa><delta value=\"10\"/></mantissa><exponent><copy value=\"-2\" /></exponent></decimal>" +
            "		<decimal name=\"open\"><copy /></decimal>" +
            "		<decimal name=\"close\"><copy /></decimal>" + "	</template>" +
            "</templates>";
        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader();
        MessageTemplate[] templates = loader.load(new ByteArrayInputStream(
                    templateXml.getBytes()));
        MessageTemplate messageTemplate = templates[0];
        assertEquals("SampleTemplate", messageTemplate.getName());
        assertEquals(7, messageTemplate.getFieldCount());
        assertScalarField(messageTemplate, 1, Type.DECIMAL, "bid",
            new TwinOperator(Operator.COPY, Operator.DELTA),
            new TwinValue(new IntegerValue(-2), ScalarValue.UNDEFINED));
        assertScalarField(messageTemplate, 2, Type.DECIMAL, "ask",
            new TwinOperator(Operator.NONE, Operator.DELTA),
            new TwinValue(ScalarValue.UNDEFINED, ScalarValue.UNDEFINED));
        assertScalarField(messageTemplate, 3, Type.DECIMAL, "high",
            new TwinOperator(Operator.COPY, Operator.NONE),
            new TwinValue(ScalarValue.UNDEFINED, ScalarValue.UNDEFINED));
        assertScalarField(messageTemplate, 4, Type.DECIMAL, "low",
            new TwinOperator(Operator.COPY, Operator.DELTA),
            new TwinValue(new IntegerValue(-2), new IntegerValue(10)));
        assertScalarField(messageTemplate, 5, Type.DECIMAL, "open",
            Operator.COPY);
        assertScalarField(messageTemplate, 6, Type.DECIMAL, "close",
            Operator.COPY);
    }

    public void testLoadTemplateThatContainsGroups() {
        String templateXml = "<templates xmlns=\"http://www.fixprotocol.org/ns/template-definition\"" +
            "	ns=\"http://www.fixprotocol.org/ns/templates/sample\">" +
            "	<template name=\"SampleTemplate\">" +
            "		<group name=\"guy\"><string name=\"First Name\"></string><string name=\"Last Name\"></string></group>" +
            "	</template>" + "</templates>";

        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader();
        MessageTemplate[] templates = loader.load(new ByteArrayInputStream(
                    templateXml.getBytes()));
        MessageTemplate messageTemplate = templates[0];

        assertEquals("SampleTemplate", messageTemplate.getName());
        assertEquals(2, messageTemplate.getFieldCount());

        assertGroup(messageTemplate, 1, "guy");
    }
    
    public void testLoadTemplateWithKey() {
        String templateXml = "<templates xmlns=\"http://www.fixprotocol.org/ns/template-definition\"" +
            "	ns=\"http://www.fixprotocol.org/ns/templates/sample\">" +
            "	<template name=\"SampleTemplate\">" +
            "		<u32 name=\"value\"><copy key=\"integer\" /></u32>" +
            "	</template>" + "</templates>";

        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader();
        MessageTemplate[] templates = loader.load(new ByteArrayInputStream(
                    templateXml.getBytes()));
        MessageTemplate messageTemplate = templates[0];

        Scalar scalar = messageTemplate.getScalar("value");
        assertEquals("integer", scalar.getKey());
    }

    public void testLoadTemplateWithUnicodeString() {
        String templateXml = "<templates xmlns=\"http://www.fixprotocol.org/ns/template-definition\"" +
            "	ns=\"http://www.fixprotocol.org/ns/templates/sample\">" +
            "	<template name=\"SampleTemplate\">" +
            "		<string name=\"name\" charset=\"unicode\" presence=\"mandatory\"><copy /></string>" +
            "		<string name=\"id\" charset=\"unicode\" presence=\"optional\"><copy /></string>" +
            "		<string name=\"location\" charset=\"ascii\" presence=\"mandatory\"><copy /></string>" +
            "		<string name=\"id2\" charset=\"ascii\" presence=\"optional\"><copy /></string>" +
            "	</template>" + "</templates>";

        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader();
        MessageTemplate[] templates = loader.load(new ByteArrayInputStream(
                    templateXml.getBytes()));
        MessageTemplate messageTemplate = templates[0];

        Scalar name = messageTemplate.getScalar("name");
        Scalar id = messageTemplate.getScalar("id");
        Scalar location = messageTemplate.getScalar("location");
        Scalar id2 = messageTemplate.getScalar("id2");
        
        assertFalse(name.isOptional());
        assertTrue(id.isOptional());
        assertFalse(location.isOptional());
        assertTrue(id2.isOptional());
        
        assertEquals(Type.UNICODE_STRING_TYPE, name.getCodecStrategy());
        assertEquals(Type.NULLABLE_UNICODE_STRING, id.getCodecStrategy());
        assertEquals(Type.ASCII_STRING_TYPE, location.getCodecStrategy());
        assertEquals(Type.NULLABLE_ASCII_STRING, id2.getCodecStrategy());
    }
    
    public void testLoadMdIncrementalRefreshTemplate() {
        InputStream templateStream = this.getClass()
                                         .getResourceAsStream("mdIncrementalRefreshTemplate.xml");
        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader();
        MessageTemplate messageTemplate = loader.load(templateStream)[0];

        assertEquals("MDIncrementalRefresh",
            messageTemplate.getMessageReference());
        assertEquals("MDRefreshSample", messageTemplate.getName());
        assertEquals(10, messageTemplate.getFieldCount());

        /********************************** TEMPLATE FIELDS **********************************/
        int index = 0;
        assertScalarField(messageTemplate, index++, Type.U32,
            "templateId", Operator.COPY);
        assertScalarField(messageTemplate, index++, Type.ASCII_STRING, "8",
            Operator.CONSTANT);
        assertScalarField(messageTemplate, index++, Type.U32, "9",
            Operator.CONSTANT);
        assertScalarField(messageTemplate, index++, Type.ASCII_STRING, "35",
            Operator.CONSTANT);
        assertScalarField(messageTemplate, index++, Type.ASCII_STRING, "49",
            Operator.CONSTANT);
        assertScalarField(messageTemplate, index++, Type.U32,
            "34", Operator.INCREMENT);
        assertScalarField(messageTemplate, index++, Type.ASCII_STRING, "52",
            Operator.DELTA);
        assertScalarField(messageTemplate, index++, Type.U32,
            "75", Operator.COPY);

        /************************************* SEQUENCE **************************************/
        assertSequence(messageTemplate, index, 17);

        Sequence sequence = (Sequence) messageTemplate.getField(index++);
        assertSequenceLengthField(sequence, "268", Type.U32,
            Operator.NONE);

        /********************************** SEQUENCE FIELDS **********************************/
        int seqIndex = 0;
        assertScalarField(sequence, seqIndex++, Type.DECIMAL, "270",
            Operator.DELTA);
        assertScalarField(sequence, seqIndex++, Type.I32, "271",
            Operator.DELTA);
        assertScalarField(sequence, seqIndex++, Type.U32, "273",
            Operator.DELTA);
        assertOptionalScalarField(sequence, seqIndex++, Type.U32,
            "346", Operator.NONE);
        assertScalarField(sequence, seqIndex++, Type.U32, "1023",
            Operator.INCREMENT);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "279",
            Operator.COPY);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "269",
            Operator.COPY);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "107",
            Operator.COPY);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "48",
            Operator.DELTA);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "276",
            Operator.COPY);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "274",
            Operator.COPY);
        assertScalarField(sequence, seqIndex++, Type.DECIMAL, "451",
            Operator.COPY);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "277",
            Operator.DEFAULT);
        assertOptionalScalarField(sequence, seqIndex++, Type.U32,
            "1020", Operator.NONE);
        assertScalarField(sequence, seqIndex++, Type.I32, "537",
            Operator.DEFAULT);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "1024",
            Operator.DEFAULT);
        assertScalarField(sequence, seqIndex++, Type.ASCII_STRING, "336",
            Operator.DEFAULT);

        assertScalarField(messageTemplate, index++, Type.ASCII_STRING, "10",
            Operator.NONE);
    }

    private void assertScalarField(FieldSet fieldSet, int fieldIndex,
        String typeName, String name, Operator operator, ScalarValue defaultValue) {
        Scalar field = (Scalar) fieldSet.getField(fieldIndex);
        assertScalarField(field, typeName, name);
        assertEquals(operator, field.getOperator());
        assertEquals(defaultValue, field.getDefaultValue());
    }

    private void assertScalarField(FieldSet fieldSet, int fieldIndex,
        String typeName, String name, String operatorType) {
        Scalar field = (Scalar) fieldSet.getField(fieldIndex);
        assertScalarField(field, typeName, name);
        assertEquals(operatorType, field.getOperator().getName());
    }

    private void assertSequenceLengthField(Sequence sequence, String name,
        String typeName, String operator) {
        assertEquals(typeName, sequence.getLength().getType());
        assertEquals(name, sequence.getLength().getName());
        assertEquals(operator, sequence.getLength().getOperator().getName());
    }

    private void assertSequence(MessageTemplate messageTemplate,
        int fieldIndex, int fieldCount) {
        Sequence sequence = (Sequence) messageTemplate.getField(fieldIndex);
        assertEquals(fieldCount, sequence.getFieldCount());
    }

    private void assertGroup(MessageTemplate messageTemplate, int fieldIndex,
        String name) {
        Group currentGroup = (Group) messageTemplate.getField(fieldIndex);
        assertEquals(name, currentGroup.getName());
    }

    private void assertOptionalScalarField(FieldSet fieldSet, int fieldIndex,
        String typeName, String name, String operator) {
        Scalar field = (Scalar) fieldSet.getField(fieldIndex);
        assertScalarField(field, typeName, name);
        assertEquals(operator, field.getOperator().getName());
        assertTrue(field.isOptional());
    }

    private void assertScalarField(Scalar field, String typeName, String name) {
        assertEquals(name, field.getName());
        assertEquals(typeName, field.getType());
    }
}
