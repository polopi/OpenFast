package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.codec.FieldCodec;
import org.openfast.codec.StringCodec;
import org.openfast.dictionary.DictionaryEntry;
import org.openfast.error.FastConstants;
import org.openfast.template.operator.DictionaryOperator;

public class TailAsciiCodec extends DictionaryOperatorStringCodec implements FieldCodec {
    protected TailAsciiCodec(DictionaryEntry dictionaryEntry, DictionaryOperator operator, StringCodec stringCodec) {
        super(dictionaryEntry, operator, stringCodec);
    }

    @Override
    public void decode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (stringCodec.isNull(buffer)) {
            dictionaryEntry.setNull();
            return;
        }
        String baseValue = getBaseValue();
        String encodedValue = stringCodec.decode(buffer);
        String value;
        if (encodedValue.length() > baseValue.length())
            value = encodedValue;
        else
            value = baseValue.substring(0,baseValue.length() - encodedValue.length()) + encodedValue;
        dictionaryEntry.set(value);
        object.set(index, value);
    }

    @Override
    public void decodeEmpty(EObject object, int index, Context context) {
        object.set(index, getBaseValue());
    }

    @Override
    public void encode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (!object.isDefined(index)) {
            buffer.put(Fast.NULL);
            dictionaryEntry.setNull();
            return;
        }
        String value = object.getString(index);
        String baseValue = getBaseValue();
        if (value.length() < baseValue.length())
            context.getErrorHandler().error(FastConstants.D3_CANT_ENCODE_VALUE, "The value " + value
                    + " cannot be encoded by a tail operator with previous value " + baseValue);
        dictionaryEntry.set(value);
        if (value.length() > baseValue.length()) {
            stringCodec.encode(buffer, value);
            return;
        }
        int i = 0;
        while (i < value.length() && value.charAt(i) == baseValue.charAt(i))
            i++;
        if (i == value.length())
            return;
        stringCodec.encode(buffer, value.substring(i));
    }

    private String getBaseValue() {
        if (dictionaryEntry.isNull())
            return "";
        if (dictionaryEntry.isDefined())
            return dictionaryEntry.getString();
        if (operator.hasDefaultValue())
            return operator.getDefaultValue(); 
        return "";
    }

    @Override
    public int getLength(ByteBuffer buffer) {
        return stringCodec.getLength(buffer);
    }
}
