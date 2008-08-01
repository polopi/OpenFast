package org.openfast.codec;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.lasalletech.entity.EObjectList;
import org.lasalletech.entity.EmptyEObject;
import org.lasalletech.entity.EntityType;
import org.openfast.Context;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class BasicSequenceCodec implements FieldCodec {
    public class IntEObjectWrapper extends EmptyEObject {
        private final int value;
        
        public IntEObjectWrapper(int value) {
            this.value = value;
        }
        @Override
        public int getInt(int index) {
            return value;
        }
        
        @Override
        public boolean isDefined(int index) {
            return true;
        }
    }

    private final FieldCodec lengthCodec;
    private final Scalar lengthScalar;
    private final BasicGroupCodec groupCodec;
    
    public BasicSequenceCodec(MessageTemplate template, Field field, FastImplementation implementation,
            DictionaryRegistry dictionaryRegistry, BasicCodecFactory basicCodecFactory) {
        lengthScalar = ((Sequence) ((EntityType)field.getType()).getEntity()).getLength();
        lengthCodec = basicCodecFactory.createScalarCodec(template, lengthScalar, implementation, dictionaryRegistry);
        groupCodec = (BasicGroupCodec) basicCodecFactory.createGroupCodec(template, field, implementation, dictionaryRegistry);
    }

    public void decode(EObject object, int index, ByteBuffer buffer, BitVectorReader pmapReader, Context context) {
    }

    public void encode(EObject object, int index, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context) {
        if (!object.isDefined(index)) {
            lengthCodec.encode(EObject.EMPTY, 0, buffer, pmapBuilder, context);
            return;
        }
        EObjectList list = object.getList(index);
        EObject wrapper = new IntEObjectWrapper(list.size());
        lengthCodec.encode(wrapper, 0, buffer, pmapBuilder, context);
        for (EObject o : list) {
            groupCodec.encode(o, buffer, context);
        }
    }

    public int getLength(ByteBuffer buffer, BitVectorReader reader) {
        return 0;
    }
}
