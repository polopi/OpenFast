package org.openfast.debug;

import java.io.PrintWriter;

import org.openfast.ByteUtil;
import org.openfast.FieldValue;
import org.openfast.template.Field;
import org.openfast.template.Group;

public class BasicDecodeTrace implements Trace {
	private String indent = "";
	private PrintWriter out;// = new PrintWriter(System.out);

	public void groupStart(Group group) {
		print(group);
		moveDown();
	}
	
	private void moveDown() {
		indent += "  ";
	}

	private void moveUp() {
		indent = indent.substring(0, indent.length()-2);
	}

	private void print(Object object) {
		out.print(indent);
		out.println(object);
	}

	public void groupEnd() {
		moveUp();
	}

	public void field(Field field, FieldValue value, FieldValue decodedValue, byte[] encoding, int pmapIndex) {
		StringBuilder scalarDecode = new StringBuilder();
		scalarDecode.append(field.getName()).append(": ");
		scalarDecode.append(ByteUtil.convertByteArrayToBitString(encoding));
		scalarDecode.append(" -> ").append(value).append('(').append(decodedValue).append(')');
		print(scalarDecode);
	}

	public void pmap(byte[] bytes) {
		print("PMAP: " + ByteUtil.convertByteArrayToBitString(bytes));
	}

	public void setWriter(PrintWriter writer) {
		this.out = writer;
	}
}