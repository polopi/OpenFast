package org.openfast;

import java.util.Date;

public class DateValue extends ScalarValue {
	private static final long serialVersionUID = 1L;
	public final Date value;

	public DateValue(Date date) {
		this.value = date;
	}

	public long toLong() {
		return value.getTime();
	}
	
	public String toString() {
		return value.toString();
	}
	
	public boolean equals(Object other) {
		if (other == this) return true;
		if (other == null || !(other instanceof DateValue)) return false;
		return equals((DateValue) other);
	}
	
	private boolean equals(DateValue other) {
		return other.value.equals(value);
	}

    public int hashCode() {
    	return value.hashCode();
    }
}