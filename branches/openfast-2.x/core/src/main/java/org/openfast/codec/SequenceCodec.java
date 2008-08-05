package org.openfast.codec;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObjectList;
import org.openfast.Context;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public interface SequenceCodec extends FieldCodec {
    void encode(EObjectList objects, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context);
    EObjectList decode(ByteBuffer buffer, BitVectorReader pmapReader, Context context);
}
