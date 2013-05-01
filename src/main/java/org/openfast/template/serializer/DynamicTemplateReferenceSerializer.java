package org.openfast.template.serializer;

import org.openfast.template.DynamicTemplateReference;
import org.openfast.template.Field;
import org.openfast.util.XmlWriter;

public class DynamicTemplateReferenceSerializer implements FieldSerializer {
    @Override
	public boolean canSerialize(Field field) {
        return field instanceof DynamicTemplateReference;
    }

    @Override
	public void serialize(XmlWriter writer, Field field, SerializingContext context) {
        writer.start("templateRef");
        writer.end();
    }
}
