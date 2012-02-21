package org.openfast.template.serializer;

import java.util.List;
import org.openfast.Node;
import org.openfast.QName;
import org.openfast.ScalarValue;
import org.openfast.error.FastConstants;
import org.openfast.template.Field;
import org.openfast.template.Group;
import org.openfast.template.Scalar;
import org.openfast.util.XmlWriter;

public abstract class AbstractFieldSerializer implements FieldSerializer {
    protected static void writeCommonAttributes(XmlWriter writer, Field field, SerializingContext context) {
    	QName fieldQName = field.getQName();
        writer.addAttribute("name", fieldQName.getName());
        String fieldQNameNamespace = fieldQName.getNamespace();
        if (!context.getNamespace().equals(fieldQNameNamespace))
        writer.addAttribute("ns", fieldQNameNamespace);
        String fieldId = field.getId();
        if (fieldId != null)
            writer.addAttribute("id", fieldId);
        if (field.isOptional())
            writer.addAttribute("presence", "optional");
    }

    protected static void writeOperator(XmlWriter writer, Scalar scalar, SerializingContext context) {
        writer.start(scalar.getOperator().getName());
        String scalarDictionary = scalar.getDictionary();
        if (!scalarDictionary.equals(context.getDictionary())) {
            writer.addAttribute("dictionary", scalarDictionary);
        }
    	QName scalarKey = scalar.getKey();
        if (!scalarKey.equals(scalar.getQName())) {
            writer.addAttribute("key", scalarKey.getName());
            String scalarKeyNamespace = scalarKey.getNamespace();
            if (!context.getNamespace().equals(scalarKeyNamespace))
                writer.addAttribute("ns", scalarKeyNamespace);
        }
        ScalarValue scalarDefaultValue = scalar.getDefaultValue();
        if (!scalarDefaultValue.isUndefined()) {
            writer.addAttribute("value", scalarDefaultValue.serialize());
        }
        writer.end();
    }

    protected static void writeChildren(XmlWriter writer, SerializingContext context, Group group) {
        for (int i = 0; i < group.getFieldCount(); ++i) {
            context.serialize(writer, group.getField(i));
        }
    }

    protected static void writeTypeReference(XmlWriter writer, Group group, SerializingContext context) {
    	QName groupTypeReference = group.getTypeReference();
        if (groupTypeReference != null) {
            writer.start("typeRef");
            writer.addAttribute("name", groupTypeReference.getName());
            if (!groupTypeReference.getNamespace().equals(context.getNamespace()))
                writer.addAttribute("ns", groupTypeReference.getNamespace());
            writer.end();
        }
    }
    
    protected static void writeLength(XmlWriter writer, Node node, SerializingContext context) {
        List<Node> lengthNodes = node.getChildren(FastConstants.LENGTH_FIELD);
        if (!lengthNodes.isEmpty()) {
            Node lengthNode = lengthNodes.get(0);
            writer.start("length");
            writer.addAttribute("name", lengthNode.getAttribute(FastConstants.LENGTH_NAME_ATTR));
            if (lengthNode.hasAttribute(FastConstants.LENGTH_NS_ATTR)) {
                String namespace = lengthNode.getAttribute(FastConstants.LENGTH_NS_ATTR);
                if (!namespace.equals(context.getNamespace()))
                    writer.addAttribute("ns", namespace);
            }
            if (lengthNode.hasAttribute(FastConstants.LENGTH_ID_ATTR))
                writer.addAttribute("id", lengthNode.getAttribute(FastConstants.LENGTH_ID_ATTR));
            writer.end();
        }
    }
}
