package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.codec.FieldCodec;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class MandatoryConstantStringCodec implements FieldCodec {
    private final String defaultValue;

    public MandatoryConstantStringCodec(String defaultValue) {
        this.defaultValue = defaultValue;
    }
    public void decode(EObject object, int index, ByteBuffer buffer, BitVectorReader reader, Context context) {
        object.set(index, defaultValue);
    }
    public void encode(EObject object, int index, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context) {
    }
    public int getLength(ByteBuffer buffer, BitVectorReader reader) {
        return 0;
    }
}