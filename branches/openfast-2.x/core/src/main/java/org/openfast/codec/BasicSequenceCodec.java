package org.openfast.codec;

import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.lasalletech.entity.EObjectList;
import org.lasalletech.entity.EmptyEObject;
import org.lasalletech.entity.EntityType;
import org.lasalletech.entity.simple.SimpleEObjectList;
import org.openfast.Context;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class BasicSequenceCodec implements SequenceCodec {
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
    
    public BasicSequenceCodec(MessageTemplate template, Field field, FastImplementation implementation, DictionaryRegistry dictionaryRegistry) {
        lengthScalar = ((Sequence) ((EntityType)field.getType()).getEntity()).getLength();
        CodecFactory codecFactory = implementation.getCodecFactory();
        lengthCodec = codecFactory.createScalarCodec(template, lengthScalar, implementation, dictionaryRegistry);
        groupCodec = (BasicGroupCodec) codecFactory.createGroupCodec(template, field, implementation, dictionaryRegistry);
    }

    public void decode(EObject object, int index, ByteBuffer buffer, BitVectorReader pmapReader, Context context) {
    }

    public void encode(EObject object, int index, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context) {
        if (!object.isDefined(index)) {
            lengthCodec.encode(EObject.EMPTY, 0, buffer, pmapBuilder, context);
            return;
        }
        EObjectList list = object.getList(index);
        encode(list, buffer, pmapBuilder, context);
    }

    public int getLength(ByteBuffer buffer, BitVectorReader reader) {
        return 0;
    }

    public EObjectList decode(ByteBuffer buffer, BitVectorReader pmapReader, Context context) {
        IntegerPlaceholder placeholder = new IntegerPlaceholder();
        lengthCodec.decode(placeholder, 0, buffer, pmapReader, context);
        if (placeholder.isNull())
            return null;
        EObjectList list = new SimpleEObjectList();
        for (int i=0; i<placeholder.getValue(); i++) {
            EObject object = groupCodec.decode(buffer, pmapReader, context);
            list.add(object);
        }
        return list;
    }

    public void encode(EObjectList objects, ByteBuffer buffer, BitVectorBuilder pmapBuilder, Context context) {
        EObject wrapper = new IntEObjectWrapper(objects.size());
        lengthCodec.encode(wrapper, 0, buffer, pmapBuilder, context);
        for (EObject o : objects) {
            groupCodec.encode(o, buffer, context);
        }
    }
}
