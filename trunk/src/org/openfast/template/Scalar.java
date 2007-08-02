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


package org.openfast.template;

import java.io.InputStream;

import org.openfast.BitVectorBuilder;
import org.openfast.Context;
import org.openfast.FieldValue;
import org.openfast.Global;
import org.openfast.ScalarValue;
import org.openfast.error.FastConstants;
import org.openfast.error.FastException;
import org.openfast.template.operator.Operator;
import org.openfast.template.operator.OperatorCodec;
import org.openfast.template.type.Type;
import org.openfast.template.type.codec.TypeCodec;


public class Scalar extends Field {
    private final Operator operator;
    private final OperatorCodec operatorCodec;
    private final Type type;
    private final TypeCodec typeCodec;
    private String dictionary;
    private ScalarValue defaultValue = ScalarValue.UNDEFINED;
    private final ScalarValue initialValue;

    /**
     * Scalar constructor - sets the dictionary as global and validates the entries 
     * @param name The name of Scalar as a string
     * @param typeName The name of the type as a string
     * @param operator Which operator object is being used
     * @param defaultValue The default value of the ScalarValue
     * @param optional Determines if the Scalar is required or not for the data
     */
    public Scalar(String name, Type type, Operator operator, ScalarValue defaultValue, boolean optional) {
        super(name, optional);
        this.operator = operator;
        this.operatorCodec = operator.getCodec(type);
        this.dictionary = "global";
        this.defaultValue = (defaultValue == null) ? ScalarValue.UNDEFINED : defaultValue;
        this.type = type;
        this.typeCodec = type.getCodec(operator, optional);
        this.initialValue = ((defaultValue == null) || defaultValue.isUndefined()) 
        								? this.type.getDefaultValue()
                                        : defaultValue;
        operator.validate(this);
    }

    /**
     * Scalar constructor - sets the dictionary as global and validates the entries 
     * @param name The name of Scalar as a string
     * @param typeName The name of the type as a string
     * @param operatorCodec Which operatorCodec object is being used
     * @param defaultValue The default value of the ScalarValue
     * @param optional Determines if the Scalar is required or not for the data
     */
    public Scalar(String name, Type type, OperatorCodec operatorCodec, ScalarValue defaultValue, boolean optional) {
        super(name, optional);
        this.operator = operatorCodec.getOperator();
        this.operatorCodec = operatorCodec;
        this.dictionary = "global";
        this.defaultValue = (defaultValue == null) ? ScalarValue.UNDEFINED : defaultValue;
        this.type = type;
        this.typeCodec = type.getCodec(operator, optional);
        this.initialValue = ((defaultValue == null) || defaultValue.isUndefined()) 
        								? this.type.getDefaultValue()
                                        : defaultValue;
        operator.validate(this);

	}

	/**
	 * 
	 * @return Returns the type as a string
	 */
    public Type getType() {
        return type;
    }

    /**
     * 
     * @return Returns the Operator object
     */
    public OperatorCodec getOperatorCodec() {
        return operatorCodec;
    }

    /**
     * 
     * @return Returns the operator name as a string
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * @param value The Field value
     * @param template The Group object
     * @param context The previous object to keep the data in sync
     * @param presenceMapBuilder The BitVector builder
     * @return 
     * @throw Throws RuntimeException if the encoding fails - will print to console the name of the scalar to fail
     */
    public byte[] encode(FieldValue fieldValue, Group template, Context context, BitVectorBuilder presenceMapBuilder) {
        ScalarValue priorValue = (ScalarValue) context.lookup(getDictionary(), template, getKey());
        ScalarValue value = (ScalarValue) fieldValue;
        if (!operatorCodec.canEncode(value, this))
        	Global.handleError(FastConstants.D3_CANT_ENCODE_VALUE, "The scalar " + this + " cannot encode the value " + value);
        ScalarValue valueToEncode = operatorCodec.getValueToEncode((ScalarValue) value, priorValue, this, presenceMapBuilder);

        if (operator.shouldStoreValue(value)) {
        	context.store(getDictionary(), template, getKey(), (ScalarValue) value);
        }

        if (valueToEncode == null) {
            return new byte[0];
        }

        return typeCodec.encode(valueToEncode);
    }

    /**
     * 
     * @return Returns the dictionary as a string
     */
    public String getDictionary() {
        return dictionary;
    }

    /**
     * 
     * @param newValue
     * @param previousValue
     * @return
     */
    public ScalarValue decodeValue(ScalarValue newValue,
        ScalarValue previousValue) {
        return operatorCodec.decodeValue(newValue, previousValue, this);
    }

    /**
     * 
     * @return Returns the defaultValue of the current ScalarValue
     */
    public ScalarValue getDefaultValue() {
        return defaultValue;
    }

    /**
     * 
     * @param in The InputStream to be decoded
     * @param previousValue 
     * @return 
     */
    public ScalarValue decode(InputStream in, ScalarValue previousValue) {
    	if (!operatorCodec.shouldDecodeType()) {
            return operatorCodec.decodeValue(null, null, this);
        }

        return decodeValue(typeCodec.decode(in), previousValue);
    }

    /**
     * 
     * @param previousValue The previousValue of the ScalarValue
     * @return Depending on the operator, various ScalarValues could be returned
     */
    public ScalarValue decode(ScalarValue previousValue) {
        return operatorCodec.decodeEmptyValue(previousValue, this);
    }
    
    /**
     * @return Returns true
     */
    public boolean usesPresenceMapBit() {
        return operatorCodec.usesPresenceMapBit(optional);
    }

    /**
     * @return Returns true if the byte array has a length 
     */
    public boolean isPresenceMapBitSet(byte[] encoding, FieldValue fieldValue) {
        return operatorCodec.isPresenceMapBitSet(encoding, fieldValue);
    }

    /**
     * 
     * @param in The InputStream to be decoded
     * @param template The Group object
     * @param context The previous object to keep the data in sync
     * @param present 
     * @return Returns the null if the Operator is constant and the optional boolean is true and the present boolean is true,
     * otherwise decodes the previousValue and returns the FieldValue object after decoding
     */
    public FieldValue decode(InputStream in, Group template, Context context,
        boolean present) {
    	try {
	        ScalarValue previousValue = context.lookup( getDictionary(), template, getKey());
	        validateDictionaryTypeAgainstFieldType(previousValue, this.type);
	        ScalarValue value;
	
	        if (present) {
	            value = decode(in, previousValue);
	        } else {
	            value = decode(previousValue);
	        }
	        
	        validateDecodedValueIsCorrectForType(value, type);
	
	        if (!((getOperator() == Operator.DELTA) && (value == null))) {
	            context.store(getDictionary(), template, getKey(), value);
	        }
	
	        return value;
    	} catch (FastException e) {
    		throw new FastException("Error occurred while decoding " + this, e.getCode(), e);
    	}
    }

    /**
     * Validate the passed ScalarValue and the Type objects
     * @param value The value to be validated
     * @param type The type to be validated
     */
    private void validateDecodedValueIsCorrectForType(ScalarValue value, Type type) {
    	if (value == null) return;
    	type.validateValue(value);
	}

    /**
     * 
     * @param previousValue
     * @param type The type to be validated
     */
	private void validateDictionaryTypeAgainstFieldType(ScalarValue previousValue, Type type) {
    	if (previousValue == null || previousValue.isUndefined()) return;
    	if (!type.isValueOf(previousValue)) {
    		Global.handleError(FastConstants.D4_INVALID_TYPE, "The value \"" + previousValue + "\" is not valid for the type " + type);
    	}
	}

	/**
     * Sets the dictionary to the passed string
     * @param dictionary The string to be stored as the dictionary
     */
    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    /**
     * @return Returns the string 'Scalar [name=X, operator=X, dictionary=X]'
     */
    public String toString() {
        return "Scalar [name=" + name + ", operator=" + operator + ", type=" + type +
        ", dictionary=" + dictionary + "]";
    }

    /**
     * @return Returns the class of the current ScalarValue
     */
    public Class getValueType() {
        return ScalarValue.class;
    }

    /**
     * @param value Creates a FieldValue of the passed value
     * @return Returns the FieldValue object with the passed value
     */
    public FieldValue createValue(String value) {
        return type.getValue(value);
    }

    /**
     * @return Returns the string 'scalar'
     */
    public String getTypeName() {
        return "scalar";
    }

    /**
     * 
     * @return Returns the initialValue of the current ScalarValue object
     */
    public ScalarValue getInitialValue() {
        return initialValue;
    }
    
    /**
     * 
     * @return Returns the type of the Codec
     */
    public TypeCodec getTypeCodec() {
    	return typeCodec;
    }

	public String serialize(ScalarValue value) {
		return type.serialize(value);
	}
}
