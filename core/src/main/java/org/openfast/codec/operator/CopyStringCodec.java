package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.codec.StringCodec;
import org.openfast.dictionary.DictionaryEntry;
import org.openfast.template.operator.DictionaryOperator;

public class CopyStringCodec extends DictionaryOperatorStringCodec {
    public CopyStringCodec(DictionaryEntry dictionaryEntry, DictionaryOperator operator, StringCodec stringCodec) {
        super(dictionaryEntry, operator, stringCodec);
    }

    public int getLength(ByteBuffer buffer) {
        return stringCodec.getLength(buffer);
    }

    public void decode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (stringCodec.isNull(buffer)) {
            dictionaryEntry.setNull();
            return;
        }
        String value = stringCodec.decode(buffer);
        object.set(index, value);
        dictionaryEntry.set(value);
    }

    public void encode(EObject object, int index, ByteBuffer buffer, Context context) {
        boolean dictionaryUndefined = !dictionaryEntry.isDefined();
        boolean dictionaryNull = dictionaryEntry.isNull();
        if (!object.isDefined(index)) {
            dictionaryEntry.setNull();
            if ((dictionaryUndefined && !operator.hasDefaultValue()) || dictionaryNull) {
                return;
            } else {
                buffer.put(Fast.NULL);
                return;
            }
        }
        String value = object.getString(index);
        if (dictionaryUndefined) {
            if ((operator.hasDefaultValue() && operator.getDefaultValue().equals(value))) {
                dictionaryEntry.set(value);
                return;
            }
        } else if (!dictionaryNull) {
            if (value.equals(dictionaryEntry.getString())) {
                dictionaryEntry.set(value);
                return;
            }
        }
        stringCodec.encode(buffer, value);
        dictionaryEntry.set(value);
    }

    public void decodeEmpty(EObject object, int index, Context context) {
        if (dictionaryEntry.isNull())
            return;
        if (dictionaryEntry.isDefined()) {
            object.set(index, dictionaryEntry.getString());
        } else if (operator.hasDefaultValue())
            object.set(index, operator.getDefaultValue());
        dictionaryEntry.set(operator.getDefaultValue());
    }
}
