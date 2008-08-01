package org.openfast.codec.operator;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.codec.FieldCodec;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public abstract class AlwaysPresentCodec implements FieldCodec {

    public void decode(EObject object, int index, ByteBuffer buffer, BitVectorReader reader, Context context) {
        decode(object, index, buffer, context);
    }

    public void encode(EObject object, int index, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context) {
        encode(object, index, buffer, context);
    }
    
    public int getLength(ByteBuffer buffer, BitVectorReader reader) {
        return getLength(buffer);
    }
    
    public abstract int getLength(ByteBuffer buffer);

    public abstract void decode(EObject object, int index, ByteBuffer buffer, Context context);
    public abstract void encode(EObject object, int index, ByteBuffer buffer, Context context);
}
