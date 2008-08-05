package org.openfast.codec.operator;

import static org.openfast.test.OpenFastTestCase.assertEquals;
import java.nio.ByteBuffer;
import junit.framework.Assert;
import org.lasalletech.entity.QName;
import org.openfast.ByteUtil;
import org.openfast.Context;
import org.openfast.Fast;
import org.openfast.Message;
import org.openfast.codec.CodecFactory;
import org.openfast.codec.SinglePresenceMapEntryFieldCodec;
import org.openfast.dictionary.BasicDictionaryRegistry;
import org.openfast.dictionary.DictionaryEntry;
import org.openfast.dictionary.DictionaryRegistry;
import org.openfast.fast.FastTypes;
import org.openfast.fast.impl.FastImplementation;
import org.openfast.template.Field;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;

public class FastLongOperatorTestHarness {

    public static final QName KEY = new QName("any", "thing");
    public static final int UNDEFINED = Integer.MIN_VALUE;
    public static final int NULL = Integer.MIN_VALUE + 1;
    public static final int NO_INITIAL_VALUE = Integer.MIN_VALUE + 2;
    public static final int INITIAL_VALUE = Integer.MIN_VALUE + 3;
    private final SinglePresenceMapEntryFieldCodec noDefaultCodec;
    private final SinglePresenceMapEntryFieldCodec defaultCodec;
    private final Scalar noDefaultScalar;
    private final Scalar defaultScalar;
    private final DictionaryRegistry dictionaryRegistry;

    public FastLongOperatorTestHarness(Scalar defaultScalar, Scalar noDefaultScalar) {
        CodecFactory factory = FastImplementation.getDefaultVersion().getCodecFactory();
        this.noDefaultScalar = noDefaultScalar;
        this.dictionaryRegistry = new BasicDictionaryRegistry(FastImplementation.getDefaultVersion().getDictionaryTypeRegistry());
        this.noDefaultCodec = (SinglePresenceMapEntryFieldCodec) factory.createScalarCodec(null, noDefaultScalar, FastImplementation.getDefaultVersion(), dictionaryRegistry);
        this.defaultScalar = defaultScalar;
        this.defaultCodec = (SinglePresenceMapEntryFieldCodec) factory.createScalarCodec(null, defaultScalar, FastImplementation.getDefaultVersion(), dictionaryRegistry);
    }

    public void assertDecodeNull(int initialValue, int dictionaryState) {
        SinglePresenceMapEntryFieldCodec codec = getCodec(initialValue);
        Context context = new Context();
        initDictionary(getScalar(initialValue), dictionaryState);
        MessageTemplate template = Fast.SIMPLE.createMessageTemplate(QName.NULL, new Field[] { new Scalar(QName.NULL, FastTypes.U32, null, true) });
        Message message = template.newMessage();
        codec.decodeEmpty(message, 0, context);
        Assert.assertFalse(message.isDefined(0));
    }

    public void assertDecodeNull(int initialValue, int dictionaryState, String encoded) {
        SinglePresenceMapEntryFieldCodec codec = getCodec(initialValue);
        Context context = new Context();
        initDictionary(getScalar(initialValue), dictionaryState);
        MessageTemplate template = Fast.SIMPLE.createMessageTemplate(QName.NULL, new Field[] { new Scalar(QName.NULL, FastTypes.U32, null, true) });
        Message message = template.newMessage();
        ByteBuffer encodedBytes = ByteBuffer.wrap(ByteUtil.convertBitStringToFastByteArray(encoded));
        codec.decode(message, 0, encodedBytes, context);
        Assert.assertFalse(message.isDefined(0));
    }

    public void assertDecode(int expectedValue, int initialValue, int dictionaryState) {
        SinglePresenceMapEntryFieldCodec codec = getCodec(initialValue);
        Context context = new Context();
        initDictionary(getScalar(initialValue), dictionaryState);
        MessageTemplate template = Fast.SIMPLE.createMessageTemplate(QName.NULL, new Field[] { new Scalar(QName.NULL, FastTypes.U32, null, true) });
        Message message = template.newMessage();
        codec.decodeEmpty(message, 0, context);
        Assert.assertEquals(expectedValue, message.getInt(0));
    }

    public void assertDecode(int expectedValue, int initialValue, int dictionaryState, String encoded) {
        SinglePresenceMapEntryFieldCodec codec = getCodec(initialValue);
        Context context = new Context();
        initDictionary(getScalar(initialValue), dictionaryState);
        Scalar scalar = new Scalar(QName.NULL, FastTypes.U32, null, true);
        MessageTemplate template = Fast.SIMPLE.createMessageTemplate(QName.NULL, new Field[] { scalar });
        Message message = template.newMessage();
        ByteBuffer encodedBytes = ByteBuffer.wrap(ByteUtil.convertBitStringToFastByteArray(encoded));
        codec.decode(message, 0, encodedBytes, context);
        Assert.assertEquals(expectedValue, message.getInt(0));
        
    }
    
    private void initDictionary(Scalar scalar, int dictionaryState) {
        dictionaryRegistry.reset();
        DictionaryEntry entry = dictionaryRegistry.get(Fast.GLOBAL).getEntry(scalar);
        if (dictionaryState == NULL) {
            entry.setNull();
        } else if (dictionaryState != UNDEFINED){
            entry.set(dictionaryState);
        }
    }

    private SinglePresenceMapEntryFieldCodec getCodec(int initialValue) {
        SinglePresenceMapEntryFieldCodec codec;
        if (initialValue == NO_INITIAL_VALUE) {
            codec = noDefaultCodec;
        } else {
            codec = defaultCodec;
        }
        return codec;
    }

    private Scalar getScalar(int initialValue) {
        if (initialValue == NO_INITIAL_VALUE) {
            return noDefaultScalar;
        }
        return defaultScalar;
    }

    public void assertEncode(String encoded, int initialValue, int dictionaryState) {
        SinglePresenceMapEntryFieldCodec codec = getCodec(initialValue);
        Context context = new Context();
        Scalar scalar = getScalar(initialValue);
        initDictionary(scalar, dictionaryState);
        MessageTemplate template = Fast.SIMPLE.createMessageTemplate(QName.NULL, new Field[] { new Scalar(QName.NULL, FastTypes.U32, null, true) });
        Message message = template.newMessage();
        ByteBuffer buffer = ByteBuffer.allocate(32);
        codec.encode(message, 0, buffer, context);
        assertEquals(encoded, buffer);
    }
    
    public void assertEncode(String encoded, int initialValue, int dictionaryState, int value) {
        SinglePresenceMapEntryFieldCodec codec = getCodec(initialValue);
        Context context = new Context();
        initDictionary(getScalar(initialValue), dictionaryState);
        MessageTemplate template = Fast.SIMPLE.createMessageTemplate(QName.NULL, new Field[] { new Scalar(QName.NULL, FastTypes.U32, null, true) });
        Message message = template.newMessage();
        message.set(0, value);ByteBuffer buffer = ByteBuffer.allocate(32);;
        codec.encode(message, 0, buffer, context);
        assertEquals(encoded, buffer);
    }

}
