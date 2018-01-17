package org.openfast.template.type;

import org.openfast.template.type.codec.TypeCodec;

public class EnumType extends IntegerType {

    public EnumType(String typeName, long maxValue) {
        super(typeName, 0, maxValue, TypeCodec.INTEGER, TypeCodec.NULLABLE_INTEGER);
    }
}
