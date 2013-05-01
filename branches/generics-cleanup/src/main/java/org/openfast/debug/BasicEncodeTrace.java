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
package org.openfast.debug;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import org.openfast.ByteUtil;
import org.openfast.FieldValue;
import org.openfast.template.Field;
import org.openfast.template.Group;

public class BasicEncodeTrace implements Trace {

    private Stack<TraceGroup> stack = new Stack<>();
    private PrintWriter out = new PrintWriter(System.out);

    @Override
	public void groupStart(Group group) {
        TraceGroup traceGroup = new TraceGroup(group);
        if (!stack.isEmpty())
            stack.peek().addGroup(traceGroup);
        stack.push(traceGroup);
    }

    @Override
	public void field(Field field, FieldValue value, FieldValue encoded, byte[] encoding, int pmapIndex) {
        stack.peek().addField(field, value, encoded, pmapIndex, encoding);
    }

    @Override
	public void groupEnd() {
        TraceGroup group = (TraceGroup) stack.pop();
        if (stack.isEmpty()) {
            out.println(group);
        }
    }

    @Override
	public void pmap(byte[] pmap) {
        stack.peek().setPmap(pmap);
    }

    private class TraceGroup implements TraceNode {

        private List<TraceNode> nodes;

        private byte[] pmap;

        private Group group;

        public TraceGroup(Group group) {
            this.group = group;
            this.nodes = new ArrayList<>(group.getFieldCount());
        }

        public void setPmap(byte[] pmap) {
            this.pmap = pmap;
        }

        public void addField(Field field, FieldValue value, FieldValue encoded, int fieldIndex, byte[] encoding) {
            nodes.add(new TraceField(field, value, encoded, fieldIndex, encoding));
        }

        public void addGroup(TraceGroup traceGroup) {
            nodes.add(traceGroup);
        }

        @Override
		public StringBuilder serialize(StringBuilder builder, int indent) {
			builder.append(indent(indent)).append(group.getName()).append("\n");
			int indentValue = indent + 2;
            if (pmap != null)
                builder.append(indent(indentValue)).append("PMAP: ").append(ByteUtil.convertByteArrayToBitString(pmap)).append("\n");
            for (int i = 0; i < nodes.size(); ++i) {
                nodes.get(i).serialize(builder, indentValue);
            }
            indentValue -= 2;
            return builder;
        }

        @Override
		public String toString() {
            return serialize(new StringBuilder(), 0).toString();
        }
    }

    private class TraceField implements TraceNode {
        private Field field;

        private int pmapIndex;

        private byte[] encoding;

        private FieldValue value;

        private FieldValue encoded;

        public TraceField(Field field, FieldValue value, FieldValue encoded, int pmapIndex, byte[] encoding) {
            this.field = field;
            this.value = value;
            this.encoded = encoded;
            this.pmapIndex = pmapIndex;
            this.encoding = encoding;
        }

        @Override
		public StringBuilder serialize(StringBuilder builder, int indent) {
            builder.append(indent(indent));
            builder.append(field.getName()).append("[");
            if (field.usesPresenceMapBit())
                builder.append("pmapIndex:").append(pmapIndex);
            builder.append("]: ").append(value).append(" = ").append(encoded).append(" -> ");
            builder.append(ByteUtil.convertByteArrayToBitString(encoding));
            builder.append("\n");
            return builder;
        }
    }

    private interface TraceNode {
        StringBuilder serialize(StringBuilder builder, int indent);
    }

    public String indent(int indent) {
        StringBuilder tab = new StringBuilder(indent);
        for (int i = 0; i < indent; ++i)
            tab.append(' ');
        return tab.toString();
    }

    public void setWriter(PrintWriter traceWriter) {
        this.out = traceWriter;
    }
}
