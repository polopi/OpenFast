package org.openfast.extensions;

import java.io.InputStream;
import org.openfast.BitVectorBuilder;
import org.openfast.BitVectorReader;
import org.openfast.ByteUtil;
import org.openfast.Context;
import org.openfast.FieldValue;
import org.openfast.IntegerValue;
import org.openfast.QName;
import org.openfast.ScalarValue;
import org.openfast.template.Field;
import org.openfast.template.Group;
import org.openfast.template.type.codec.TypeCodec;

public class MapScalar extends Field {
    private static final long serialVersionUID = 1L;

    public MapScalar(QName name, boolean optional, QName key) {
        super(name, key, optional);
    }

    @Override
	public FieldValue createValue(String value) {
        return null;
    }

    @Override
	public FieldValue decode(InputStream in, Group template, Context context, BitVectorReader presenceMapReader) {
        boolean newDefinition = presenceMapReader.read();
        int index = TypeCodec.UINT.decode(in).toInt();
        if (index == 0) {
            return ScalarValue.NULL;
        }
        if (!newDefinition)
            return context.getCache(getKey()).lookup(index);
        ScalarValue value = TypeCodec.ASCII.decode(in);
        context.store(getKey(), index, value);
        return value;
    }

    @Override
	public byte[] encode(FieldValue value, Group template, Context context, BitVectorBuilder presenceMapBuilder) {
        if (context.getCache(getKey()).containsValue(value)) {
            int index = context.getCache(getKey()).getIndex(value);
            byte[] encoded = TypeCodec.UINT.encode(new IntegerValue(index));
            presenceMapBuilder.skip();
            return encoded;
        } else {
            int nextIndex = context.getCache(getKey()).store(value);
            byte[] indexBytes = TypeCodec.UINT.encode(new IntegerValue(nextIndex));
            byte[] valueBytes = TypeCodec.ASCII.encode((ScalarValue) value);
            presenceMapBuilder.set();
            return ByteUtil.combine(indexBytes, valueBytes);
        }
    }

    @Override
	public String getTypeName() {
        return null;
    }

    @Override
	public Class<? extends FieldValue> getValueType() {
        return null;
    }

    @Override
	public boolean isPresenceMapBitSet(byte[] encoding, FieldValue fieldValue) {
        return false;
    }

    @Override
	public boolean usesPresenceMapBit() {
        return true;
    }
}
