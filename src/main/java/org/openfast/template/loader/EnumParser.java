package org.openfast.template.loader;

import org.w3c.dom.Element;

public class EnumParser extends ScalarParser {

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
