package org.openfast.template.type.codec;

import java.io.InputStream;
import java.util.Date;

import org.openfast.DateValue;
import org.openfast.IntegerValue;
import org.openfast.ScalarValue;
import org.openfast.util.Util;

public class DateInteger extends TypeCodec {
    private static final long serialVersionUID = 1L;

    public ScalarValue decode(InputStream in) {
        long longValue = ((ScalarValue) TypeCodec.UINT.decode(in)).toLong();
        int year = (int) (longValue / 10000);
        int month = (int) ((longValue - (year * 10000)) / 100);
        int day = (int) (longValue % 100);
        return new DateValue(Util.date(year, month, day));
    }
    public byte[] encodeValue(ScalarValue value) {
        Date date = ((DateValue) value).value;
        int intValue = Util.dateToInt(date);
        return TypeCodec.UINT.encode(new IntegerValue(intValue));
    }
    public boolean equals(Object obj) {
        return obj != null && obj.getClass() == getClass();
    }
}
