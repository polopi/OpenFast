/*
The contents of this file are subject to the Mozilla Public License
Version 1.1 (the "License"); you may not use this file except in
compliance with the License. You may obtain a copy of the License at
http://www.mozilla.org/MPL/

Software distributed under the License is distributed on an "AS IS"
basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
License for the specific language governing rights and limitations
under the License.

The Original Code is OpenFAST.

The Initial Developer of the Original Code is The LaSalle Technology
Group, LLC.  Portions created by The LaSalle Technology Group, LLC
are Copyright (C) The LaSalle Technology Group, LLC. All Rights Reserved.

Contributor(s): Jacob Northey <jacob@lasalletech.com>
                Craig Otis <cotis@lasalletech.com>
*/


/**
 *
 */
package org.openfast.template.type;

import org.openfast.IntegerValue;
import org.openfast.NumericValue;
import org.openfast.ScalarValue;

import java.io.InputStream;


public final class NullableSignedInteger extends IntegerType {
    NullableSignedInteger() { }

    public byte[] encodeValue(ScalarValue value) {
        if (value.isNull()) {
            return Type.NULL_VALUE_ENCODING;
        }

        IntegerValue intValue = (IntegerValue) value;

        if (intValue.value >= 0) {
            return Type.INTEGER.encodeValue(intValue.increment());
        } else {
            return Type.INTEGER.encodeValue(intValue);
        }
    }

    public ScalarValue decode(InputStream in) {
        NumericValue numericValue = ((NumericValue) Type.INTEGER.decode(in));
        long value = numericValue.getLong();

        if (value == 0) {
            return ScalarValue.NULL;
        }

        if (value > 0) {
            return numericValue.decrement();
        }

        return numericValue;
    }
    
    public boolean isNullable() {
    	return true;
    }
}
