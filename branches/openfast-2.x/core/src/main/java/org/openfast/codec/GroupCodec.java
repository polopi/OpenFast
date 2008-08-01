package org.openfast.codec;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.util.BitVectorReader;

public interface GroupCodec extends FieldCodec {
    void encode(EObject object, ByteBuffer buffer, Context context);
    EObject decode(ByteBuffer buffer, BitVectorReader pmapReader, Context context);
}
