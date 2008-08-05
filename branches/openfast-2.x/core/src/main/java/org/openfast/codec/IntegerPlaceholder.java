package org.openfast.codec;

import org.lasalletech.entity.EmptyEObject;

public class IntegerPlaceholder extends EmptyEObject {
    private int value;
    private boolean isNull = true;

    public int getValue() {
        return value;
    }

    public boolean isNull() {
        return isNull;
    }

    @Override
    public void set(int index, int value) {
        this.isNull = false;
        this.value = value;
    }
}
