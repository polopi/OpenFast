package org.openfast.codec;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.lasalletech.entity.Entity;
import org.openfast.Context;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.error.FastConstants;
import org.openfast.fast.FastTypes;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.util.BitVector;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class BasicGroupCodec implements GroupCodec {
    private final BitVectorCodec bitVectorCodec;
    private final FieldCodec[] fieldCodecs;
    private final Entity composite;

    public BasicGroupCodec(MessageTemplate template, Entity composite, FastImplementation implementation, DictionaryRegistry dictionaryRegistry) {
        this.bitVectorCodec = implementation.getTypeCodecRegistry().getBitVectorCodec(FastTypes.BIT_VECTOR);
        this.fieldCodecs = new FieldCodec[composite.getFieldCount()];
        this.composite = composite;
        int index = 0;
        CodecFactory codecFactory = implementation.getCodecFactory();
        for (org.lasalletech.entity.Field field : composite.getFields()) {
            if (field instanceof Scalar) {
                Scalar scalar = (Scalar) field;
                fieldCodecs[index] = codecFactory.createScalarCodec(template, scalar, implementation, dictionaryRegistry);
                index++;
            } else {
                fieldCodecs[index] = codecFactory.createCompositeCodec(template, (Field) field, implementation, dictionaryRegistry);
                index++;
            }
        }
    }

    public void decode(EObject object, int index, ByteBuffer buffer, BitVectorReader pmapReader, Context context) {
        object.set(index, decode(buffer, pmapReader, context));
    }
    
    public EObject decode(ByteBuffer buffer, BitVectorReader reader, Context context) {
        BitVector vector = bitVectorCodec.decode(buffer);
        BitVectorReader pmapReader = new BitVectorReader(vector);
        EObject o = composite.newObject();
        for (int i=0; i<fieldCodecs.length; i++) {
            fieldCodecs[i].decode(o, i, buffer, pmapReader, context);
        }
        return o;
    }

    public void encode(EObject object, int index, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context) {
        encode(object.getEObject(index), buffer, context);
    }
    
    public void encode(EObject object, ByteBuffer buffer, Context context) {
        ByteBuffer temp = context.getTemporaryBuffer();
        try {
            BitVectorBuilder pmapBuilder = new BitVectorBuilder(7); // TODO - calculate size of pmap builder
            for (int i=0; i<fieldCodecs.length; i++) {
                fieldCodecs[i].encode(object, i, temp, pmapBuilder, context);
            }
            bitVectorCodec.encode(buffer, pmapBuilder.getBitVector());
            temp.flip();
            buffer.put(temp);
        } catch (Throwable t) {
            context.getErrorHandler().error(FastConstants.GENERAL_ERROR, "Error occurred while encoding " + object, t);
        } finally {
            context.discardTemporaryBuffer(temp);
        }
    }

    public int getLength(ByteBuffer buffer, BitVectorReader reader) {
        return 0;
    }
}
