package org.openfast.template;

import org.openfast.QName;
import org.openfast.ScalarValue;
import org.openfast.template.operator.Operator;
import org.openfast.template.type.Type;

public class EnumScalar extends Scalar {

    private String[] enumValues;

    public EnumScalar(QName name, Type type, Operator operator, ScalarValue defaultValue, boolean optional, String[] enumValues) {
        super(name, type, operator, defaultValue, optional);
        this.enumValues = enumValues;
    }

    public String[] getEnumValues() {
        return enumValues;
    }
}
