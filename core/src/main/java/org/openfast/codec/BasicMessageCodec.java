package org.openfast.codec;

import java.nio.ByteBuffer;
import org.openfast.Context;
import org.openfast.Message;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.error.FastConstants;
import org.openfast.fast.FastTypes;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class BasicMessageCodec implements MessageCodec {
    private final int templateId;
    private final LongCodec uintCodec;
    private final BitVectorCodec bitVectorCodec;
    private final FieldCodec[] fieldCodecs;

    public BasicMessageCodec(int id, MessageTemplate template, FastImplementation implementation, DictionaryRegistry dictionaryRegistry, CodecFactory codecFactory) {
        this.templateId = id;
        this.uintCodec = implementation.getTypeCodecRegistry().getLongCodec(FastTypes.U32);
        this.bitVectorCodec = implementation.getTypeCodecRegistry().getBitVectorCodec(FastTypes.BIT_VECTOR);
        this.fieldCodecs = new FieldCodec[template.getFieldCount()];
        int index = 0;
        for (Field field : template.getFields()) {
            if (field instanceof Scalar) {
                Scalar scalar = (Scalar) field;
                fieldCodecs[index] = codecFactory.createScalarCodec(template, scalar, implementation, dictionaryRegistry);
                index++;
            } else {
                fieldCodecs[index] = codecFactory.createCompositeCodec(template, field, implementation, dictionaryRegistry);
                index++;
            }
        }
    }

    public void encode(ByteBuffer buffer, Message message, Context context) {
        ByteBuffer temp = context.getTemporaryBuffer();
        try {
            BitVectorBuilder pmapBuilder = new BitVectorBuilder(7); // TODO - calculate size of pmap builder
            if (context.getLastTemplateId() != templateId) {
                uintCodec.encode(temp, templateId);
                context.setLastTemplateId(templateId);
                pmapBuilder.set();
            } else {
                pmapBuilder.skip();
            }
            for (int i=0; i<fieldCodecs.length; i++) {
                fieldCodecs[i].encode(message, i, temp, pmapBuilder, context);
            }
            bitVectorCodec.encode(buffer, pmapBuilder.getBitVector());
            temp.flip();
            buffer.put(temp);
        } catch (Throwable t) {
            context.getErrorHandler().error(FastConstants.GENERAL_ERROR, "Error occurred while encoding " + message, t);
        } finally {
            context.discardTemporaryBuffer(temp);
        }
    }

    public int getLength(ByteBuffer buffer, BitVectorReader reader, Context context) {
        int length = 0;
        for (int i=0; i<fieldCodecs.length; i++) {
            length += fieldCodecs[i].getLength(buffer, reader);
        }
        return length;
    }

    public void decode(Message message, ByteBuffer buffer, BitVectorReader reader, Context context) {
        for (int i=0; i<fieldCodecs.length; i++) {
            fieldCodecs[i].decode(message, i, buffer, reader, context);
        }
    }
}
