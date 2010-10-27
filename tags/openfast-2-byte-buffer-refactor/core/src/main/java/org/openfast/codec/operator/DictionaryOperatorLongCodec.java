package org.openfast.codec.operator;

import org.openfast.codec.FieldCodec;
import org.openfast.codec.LongCodec;
import org.openfast.codec.SinglePresenceMapEntryFieldCodec;
import org.openfast.dictionary.DictionaryEntry;
import org.openfast.template.operator.DictionaryOperator;

public abstract class DictionaryOperatorLongCodec extends SinglePresenceMapEntryFieldCodec implements FieldCodec {
    protected final LongCodec longCodec;
    protected final DictionaryOperator operator;
    protected final long initialValue;
    protected final DictionaryEntry dictionaryEntry;
    
    public DictionaryOperatorLongCodec(DictionaryEntry entry, DictionaryOperator operator, LongCodec longCodec) {
        if (entry == null || operator == null || longCodec == null) throw new NullPointerException();
        this.dictionaryEntry = entry;
        this.longCodec = longCodec;
        this.operator = operator;
        this.initialValue = operator.hasDefaultValue() ? Long.parseLong(operator.getDefaultValue()) : 0;
    }
}