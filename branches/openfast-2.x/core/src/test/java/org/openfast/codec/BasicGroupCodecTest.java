package org.openfast.codec;

import static org.openfast.template.ScalarBuilder.scalar;
import java.nio.ByteBuffer;
import org.lasalletech.entity.EObject;
import org.lasalletech.entity.QName;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.dictionary.BasicDictionaryRegistry;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.fast.FastTypes;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.Group;
import org.openfast.test.OpenFastTestCase;
import org.openfast.util.BitVectorReader;

public class BasicGroupCodecTest extends OpenFastTestCase {
    FastImplementation impl = FastImplementation.getDefaultVersion();
    DictionaryRegistry dictionaryRegistry = new BasicDictionaryRegistry(impl.getDictionaryTypeRegistry());
    Group group = Fast.SIMPLE.createGroup(new QName(""), new Field[] {
        scalar("one", FastTypes.U16).build(),
        scalar("two", FastTypes.ASCII).build()
    });
    BasicGroupCodec codec = new BasicGroupCodec(null, group, impl, dictionaryRegistry);
    Context context = new Context();
    ByteBuffer buffer = ByteBuffer.allocate(32);
    
    public void testDecode() {
        ByteBuffer encoded = buffer("10000000 10000010 11000001");
        EObject object = codec.decode(encoded, BitVectorReader.INFINITE_TRUE, context);
        assertEquals(2, object.getInt(0));
        assertEquals("A", object.getString(1));
    }

    public void testEncode() {
        EObject object = group.newObject();
        object.set(0, 2);
        object.set(1, "A");
        codec.encode(object, buffer, context);
        assertEquals("10000000 10000010 11000001", buffer);
    }
}
