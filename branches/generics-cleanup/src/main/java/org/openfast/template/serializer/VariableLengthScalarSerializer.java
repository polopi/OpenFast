package org.openfast.template.serializer;

import org.openfast.template.Field;
import org.openfast.template.Scalar;
import org.openfast.template.type.Type;
import org.openfast.util.XmlWriter;

public class VariableLengthScalarSerializer extends ScalarSerializer implements FieldSerializer {

    @Override
	public boolean canSerialize(Field field) {
        if (!(field instanceof Scalar)) {
            return false;
        }
        Scalar scalar = (Scalar) field;
        return Type.STRING.equals(scalar.getType()) || 
               Type.ASCII.equals(scalar.getType()) || 
               Type.UNICODE.equals(scalar.getType()) || 
               Type.BYTE_VECTOR.equals(scalar.getType());
    }
    
    @Override
	public void serialize(XmlWriter writer, Field field, SerializingContext context) {
        Scalar scalar = writeStart(writer, field, context);
        if (scalar.getType().equals(Type.UNICODE))
            writer.addAttribute("charset", "unicode");
        writeLength(writer, scalar, context);
        writeEnd(writer, context, scalar);
    }
}
