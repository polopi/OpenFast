package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.codec.FieldCodec;
import org.openfast.codec.StringCodec;

public class NoneStringCodec extends AlwaysPresentCodec implements FieldCodec {
    private StringCodec stringCodec;

    public NoneStringCodec(StringCodec stringCodec) {
        this.stringCodec = stringCodec;
    }
    public int getLength(ByteBuffer buffer) {
        return stringCodec.getLength(buffer);
    }

    public void decode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (!stringCodec.isNull(buffer)) {
            object.set(index, stringCodec.decode(buffer));
        }
    }

    public void encode(EObject object, int index, ByteBuffer buffer, Context context) {
        if (!object.isDefined(index)) {
            buffer.put(Fast.NULL);
            return;
        }
        stringCodec.encode(buffer, object.getString(index));
    }
}
