package org.openfast.template.loader;

import org.openfast.QName;
import org.openfast.template.Define;
import org.openfast.template.Field;
import org.openfast.template.Scalar;
import org.w3c.dom.Element;

public class FieldParser extends ScalarParser {
    public static final String nodeName = "field";

    public FieldParser() {
        super(nodeName);
    }

    @Override
    protected Scalar getScalar(Element fieldNode, boolean optional, ParsingContext context) {
        final QName name = getName(fieldNode, context);
        final String typeName = getTypeName(fieldNode);
        final Scalar template;
        final Element operatorElement = getOperatorElement(fieldNode);

        if(isType(operatorElement)) {
            final Define define = context.getDefineRegistry().get(typeName);
            template = (Scalar) define.getField(0);
        } else {
            template = (Scalar) context.getFieldParser(operatorElement).parse(operatorElement, context);
        }
        return new Scalar(name, template);
    }

    @Override
    public boolean canParse(Element element, ParsingContext context) {
        return nodeName.equals(element.getNodeName());
    }

    @Override
    protected String getTypeName(Element fieldNode) {
        final Element operatorElement = getOperatorElement(fieldNode);
        return isType(operatorElement) ?
            operatorElement.getAttribute("name") :
            operatorElement.getNodeName();
    }

    private boolean isType(Element fieldNode) {
        return "type".equals(fieldNode.getNodeName());
    }
}
