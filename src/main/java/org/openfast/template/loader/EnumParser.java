package org.openfast.template.loader;

import org.openfast.template.EnumScalar;
import org.openfast.template.Scalar;
import org.w3c.dom.Element;

import java.util.ArrayList;
import java.util.List;

public class EnumParser extends ScalarParser {

    @Override
    protected Scalar getScalar(Element fieldNode, boolean optional, ParsingContext context) {
        final Scalar scalar = super.getScalar(fieldNode, optional, context);
        final String[] enumValues = getEnumValues(fieldNode);
        return new EnumScalar(getName(fieldNode, context), scalar.getType(), scalar.getOperator(), scalar.getDefaultValue(), optional, enumValues);
    }

    private String [] getEnumValues(Element fieldNode) {
        List<String> enumValues = new ArrayList<String>();
        int i = 1;
        Element element = getElement(fieldNode, i);
        while(element != null && "element".equals(element.getNodeName())) {
            enumValues.add(element.getAttribute("name"));
            i++;
            element = getElement(fieldNode, i);
        }
        return enumValues.toArray(new String[enumValues.size()]);
    }

    @Override
    protected Element getOperatorElement(Element fieldNode) {
        int i = 1;
        Element element;
        do {
            element = getElement(fieldNode, i);
            i++;
        } while (element != null && "element".equals(element.getNodeName()));
        return element;
    }

    @Override
    public boolean canParse(Element element, ParsingContext context) {
        return "enum".equals(element.getNodeName());
    }
}
