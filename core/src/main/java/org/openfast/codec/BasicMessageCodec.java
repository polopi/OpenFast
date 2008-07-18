package org.openfast.codec;

import org.lasalletech.entity.Field;
import org.openfast.ByteUtil;
import org.openfast.Context;
import org.openfast.Message;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.fast.FastTypes;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class BasicMessageCodec implements MessageCodec {
    private final int templateId;
    private final IntegerCodec uintCodec;
    private final BitVectorCodec bitVectorCodec;
    @SuppressWarnings("unchecked")
    private final FieldCodec[] fieldCodecs;

    public BasicMessageCodec(int id, MessageTemplate template, FastImplementation implementation, DictionaryRegistry dictionaryRegistry, CodecFactory codecFactory) {
        this.templateId = id;
        this.uintCodec = implementation.getTypeCodecRegistry().getIntegerCodec(FastTypes.U32);
        this.bitVectorCodec = implementation.getTypeCodecRegistry().getBitVectorCodec(FastTypes.BIT_VECTOR);
        this.fieldCodecs = new FieldCodec[template.getFieldCount()];
        int index = 0;
        for (Field field : template.getFields()) {
            if (field instanceof Scalar) {
                Scalar scalar = (Scalar) field;
                fieldCodecs[index] = codecFactory.createScalarCodec(template, scalar, implementation, dictionaryRegistry);
                index++;
            }
        }
    }

    @SuppressWarnings("unchecked")
    public int encode(byte[] buffer, int offset, Message message, Context context) {
        byte[] temp = context.getTemporaryBuffer();
        int index = 0;
        BitVectorBuilder pmapBuilder = new BitVectorBuilder(7); // TODO - calculate size of pmap builder
        if (context.getLastTemplateId() != templateId) {
            index = uintCodec.encode(temp, offset, templateId);
            context.setLastTemplateId(templateId);
            pmapBuilder.set();
        } else {
            pmapBuilder.skip();
        }
        for (int i=0; i<fieldCodecs.length; i++) {
            index = fieldCodecs[i].encode(message, i, temp, index, message.getTemplate().getField(i), pmapBuilder, context);
        }
        int pmapLen = bitVectorCodec.encode(buffer, offset, pmapBuilder.getBitVector());
        System.arraycopy(temp, 0, buffer, offset + pmapLen, index);
        context.discardTemporaryBuffer(temp);
        System.out.println(ByteUtil.convertByteArrayToBitString(buffer, offset + pmapLen + index));
        return offset + pmapLen + index;
    }

    public int getLength(byte[] buffer, int offset, BitVectorReader reader, Context context) {
        int length = 0;
        for (int i=0; i<fieldCodecs.length; i++) {
            length += fieldCodecs[i].getLength(buffer, offset, reader);
        }
        return length;
    }

    @SuppressWarnings("unchecked")
    public void decode(Message message, byte[] buffer, int offset, BitVectorReader reader, Context context) {
        for (int i=0; i<fieldCodecs.length; i++) {
            fieldCodecs[i].decode(message, i, buffer, offset, message.getTemplate().getField(i), reader, context);
        }
    }
}