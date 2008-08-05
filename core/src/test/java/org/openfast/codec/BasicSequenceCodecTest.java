package org.openfast.codec;

import static org.openfast.template.ScalarBuilder.scalar;
import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.lasalletech.entity.EObjectList;
import org.lasalletech.entity.QName;
import org.lasalletech.entity.simple.SimpleEObjectList;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.dictionary.BasicDictionaryRegistry;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.fast.FastTypes;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;
import org.openfast.test.OpenFastTestCase;
import org.openfast.util.BitVectorBuilder;
import org.openfast.util.BitVectorReader;

public class BasicSequenceCodecTest extends OpenFastTestCase {
    FastImplementation impl = FastImplementation.getDefaultVersion();
    DictionaryRegistry dictionaryRegistry = new BasicDictionaryRegistry(impl.getDictionaryTypeRegistry());
    Scalar sequenceLengthField = scalar("length", FastTypes.U16).build();
    Sequence sequence = Fast.SIMPLE.createSequence(new QName(""), sequenceLengthField, new Field[] {
        scalar("one", FastTypes.U16).build(),
        scalar("two", FastTypes.ASCII).build()
    });
    BasicSequenceCodec codec = new BasicSequenceCodec(null, Fast.SIMPLE.createField(sequence, false), impl, dictionaryRegistry);
    Context context = new Context();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
    public void testDecode() {
        ByteBuffer buffer = buffer("10000010 10000000 10000010 11000001 10000000 10000011 11000010");
        EObjectList objects = codec.decode(buffer, BitVectorReader.INFINITE_TRUE, context);
        assertEquals(2, objects.size());
    }

    public void testEncode() {
        EObjectList objects = new SimpleEObjectList();
        EObject object = sequence.newObject();
        object.set(0, 2);
        object.set(1, "A");
        objects.add(object);
        object = sequence.newObject();
        object.set(0, 3);
        object.set(1, "B");
        objects.add(object);
        codec.encode(objects, buffer, new BitVectorBuilder(7), context);
        assertEquals("10000010 10000000 10000010 11000001 10000000 10000011 11000010", buffer);
    }
}
