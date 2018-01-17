package org.openfast;

public class EnumValue extends IntegerValue {
    public EnumValue(int value) {
        super(value);
    }

    public NumericValue increment() {
        return null;
    }

    public NumericValue decrement() {
        return null;
    }

    public NumericValue subtract(NumericValue priorValue) {
        return null;
    }

    public NumericValue add(NumericValue addend) {
        return null;
    }

    public boolean equals(int value) {
        return false;
    }

    public long toLong() {
        return 0;
    }

    public int toInt() {
        return 0;
    }
}
