package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.codec.FieldCodec;
import org.openfast.codec.SinglePresenceMapEntryFieldCodec;
import org.openfast.codec.StringCodec;
import org.openfast.template.Operator;

public class DefaultStringCodec extends SinglePresenceMapEntryFieldCodec implements FieldCodec {
    private final Operator operator;
    private final StringCodec stringCodec;

    public DefaultStringCodec(Operator operator, StringCodec stringCodec) {
        this.operator = operator;
        this.stringCodec = stringCodec;
    }
    public int getLength(ByteBuffer buffer) {
        return stringCodec.getLength(buffer);
    }

    public void decode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (stringCodec.isNull(buffer))
            return;
        String value = stringCodec.decode(buffer);
        object.set(index, value);
    }

    public void decodeEmpty(EObject object, int index, Context context) {
        if (operator.hasDefaultValue())
            object.set(index, operator.getDefaultValue());
    }

    public void encode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (object.isDefined(index)) {
            if (operator.hasDefaultValue() && operator.getDefaultValue().equals(object.getString(index)))
                return;
            stringCodec.encode(buffer, object.getString(index));
        } else {
            if (!operator.hasDefaultValue())
                return;
            buffer.put(Fast.NULL);
        }
    }
    
}
