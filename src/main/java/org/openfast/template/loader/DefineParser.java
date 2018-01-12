package org.openfast.template.loader;

import org.openfast.QName;
import org.openfast.template.Define;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.w3c.dom.Element;

public class DefineParser extends GroupParser {

    /**
     * Creates a Define object from the dom define element
     *
     * @param context
     * @param element
     *            The dom element object
     * @return Returns a newly created MessageTemplate object
     */
    protected Field parse(final Element element, boolean optional, final ParsingContext context) {
        final QName defineName = getDefineName(element, context);
        try {
            final Field[] fields = parseFields(element, context);
            return createDefine(element, context, defineName, fields);
        } catch (UnresolvedStaticTemplateReferenceException e) {
            return null;
        }
    }

    @Override
    public boolean canParse(Element element, ParsingContext context) {
        return "define".equals(element.getNodeName());
    }

    private Define createDefine(Element element, ParsingContext context, QName defineName, Field[] fields) {
        Define define = new Define(defineName, fields);
        parseMore(element, define, context);
        if (element.hasAttribute("id")) {
            try {
                int defineId = Integer.parseInt(element.getAttribute("id"));
                context.getDefineRegistry().register(defineId, define);
            } catch (NumberFormatException e) {
                context.getDefineRegistry().define(define);
            }
        } else {
            context.getDefineRegistry().define(define);
        }
        return define;
    }

    private QName getDefineName(Element defineElement, ParsingContext context) {
        return new QName(defineElement.getAttribute("name"), context.getTemplateNamespace());
    }

}
