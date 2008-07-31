package org.openfast.codec.operator;

import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.codec.FieldCodec;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class OptionalConstantStringCodec implements FieldCodec {
    private final String defaultValue;

    public OptionalConstantStringCodec(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public int decode(EObject object, int index, byte[] buffer, int offset, BitVectorReader reader, Context context) {
        if (reader.read())
            object.set(index, defaultValue);
        return offset;
    }
    public int encode(EObject object, int index, byte[] buffer, int offset, BitVectorBuilder pmapBuilder, Context context) {
        if (object.isDefined(index))
            pmapBuilder.set();
        else
            pmapBuilder.skip();
        return offset;
    }
    public int getLength(byte[] buffer, int offset, BitVectorReader reader) {
        return 0;
    }
}
