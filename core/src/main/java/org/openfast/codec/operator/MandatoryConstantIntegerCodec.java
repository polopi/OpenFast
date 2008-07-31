package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.codec.FieldCodec;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class MandatoryConstantIntegerCodec implements FieldCodec {
    private final int defaultValue;

    public MandatoryConstantIntegerCodec(int defaultValue) {
        this.defaultValue = defaultValue;
    }
    public void decode(EObject object, int index, ByteBuffer buffer, BitVectorReader reader, Context context) {
        object.set(index, defaultValue);
    }
    public int encode(EObject object, int index, byte[] buffer, int offset, BitVectorBuilder pmapBuilder, Context context) {
        return offset;
    }
    public int getLength(ByteBuffer buffer, BitVectorReader reader) {
        return 0;
    }
}
