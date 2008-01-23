package org.openfast.debug;

import java.io.PrintWriter;

import org.openfast.FieldValue;
import org.openfast.template.Field;
import org.openfast.template.Group;

public interface Trace {
	void groupStart(Group group);
	void groupEnd();
	void field(Field field, FieldValue value, FieldValue encoded, byte[] encoding, int pmapIndex);
	void pmap(byte[] pmap);
	void setWriter(PrintWriter writer);
}
