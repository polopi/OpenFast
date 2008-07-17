package org.openfast.codec;

import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.template.Type;
import org.openfast.template.operator.DictionaryOperator;
import org.openfast.codec.operator.IncrementIntegerCodec;

public class BasicCodecFactory implements CodecFactory {
    public MessageCodec createCodec(int id, MessageTemplate template, TypeCodecRegistry typeCodecRegistry) {
        return new BasicMessageCodec(id, template, typeCodecRegistry, this);
    }

    public ScalarCodec createCodec(Scalar scalar, TypeCodecRegistry typeCodecRegistry) {
        if ("increment".equals(scalar.getOperator().getName())) {
            return new IncrementIntegerCodec((DictionaryOperator)scalar.getOperator(), typeCodecRegistry.getIntegerCodec((Type) scalar.getType()));
        }
        return null;
    }
}