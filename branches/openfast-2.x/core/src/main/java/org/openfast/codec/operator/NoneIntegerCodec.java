package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.codec.FieldCodec;
import org.openfast.codec.IntegerCodec;
import org.openfast.template.Scalar;

public class NoneIntegerCodec extends AlwaysPresentCodec implements FieldCodec {
    private IntegerCodec integerCodec;

    public NoneIntegerCodec(IntegerCodec integerCodec) {
        this.integerCodec = integerCodec;
    }
    public int getLength(ByteBuffer buffer) {
        return integerCodec.getLength(buffer);
    }

    public void decode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (!integerCodec.isNull(buffer)) {
            object.set(index, integerCodec.decode(buffer));
        }
    }

    public void encode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (!object.isDefined(index)) {
            buffer.put(Fast.NULL);
            return;
        }
        integerCodec.encode(buffer, object.getInt(index));
    }
}
