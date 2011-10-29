package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.codec.FieldCodec;
import org.openfast.codec.IntegerCodec;
import org.openfast.codec.SinglePresenceMapEntryFieldCodec;
import org.openfast.template.Operator;

public class DefaultIntegerCodec extends SinglePresenceMapEntryFieldCodec implements FieldCodec {
    private final Operator operator;
    private final IntegerCodec integerCodec;
    private final int defaultValue;

    public DefaultIntegerCodec(Operator operator, IntegerCodec integerCodec) {
        this.operator = operator;
        this.integerCodec = integerCodec;
        this.defaultValue = operator.hasDefaultValue() ? Integer.parseInt(operator.getDefaultValue()) : 0;
    }
    public int getLength(ByteBuffer buffer) {
        return integerCodec.getLength(buffer);
    }

    public void decode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (integerCodec.isNull(buffer))
            return;
        int value = integerCodec.decode(buffer);
        object.set(index, value);
    }

    public void decodeEmpty(EObject object, int index, Context context) {
        if (operator.hasDefaultValue())
            object.set(index, defaultValue);
    }

    public int encode(EObject object, int index, byte[] buffer, int offset, Context context) {
        if (object.isDefined(index)) {
            if (operator.hasDefaultValue() && object.getInt(index) == defaultValue)
                return offset;
            return integerCodec.encode(buffer, offset, object.getInt(index));
        } else {
            if (!operator.hasDefaultValue())
                return offset;
            buffer[offset] = Fast.NULL;
            return offset + 1;
        }
    }
    
}