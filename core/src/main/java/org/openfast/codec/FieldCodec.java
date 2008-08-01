package org.openfast.codec;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.openfast.Context;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public interface FieldCodec {
    
    /**
     * @param object       the message to decode the field value into
     * @param index        the field index to decode the value into
     * @param buffer       the encoded source buffer
     * @param pmapReader   the presence map reader
     * @param context      the current decoding context
     * @param field        the field definition that this codec was built from
     */
    @SuppressWarnings("unchecked")
    void decode(EObject object, int index, ByteBuffer buffer, BitVectorReader pmapReader, Context context);
    /**
     * @param object
     * @param index
     * @param buffer
     * @param pmapBuilder
     * @param context
     * @param field
     */
    @SuppressWarnings("unchecked")
    void encode(EObject object, int index, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context);
    int getLength(ByteBuffer buffer, BitVectorReader reader);
}
