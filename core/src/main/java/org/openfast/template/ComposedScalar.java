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

import org.lasalletech.entity.QName;
import org.openfast.template.Type;

public class ComposedScalar extends BasicField {
    private static final long serialVersionUID = 1L;
    private Scalar[] fields;
    private Type type;

    public ComposedScalar(String name, Type type, Scalar[] fields, boolean optional) {
        this(new QName(name), type, fields, optional);
    }

    public ComposedScalar(QName name, Type type, Scalar[] fields, boolean optional) {
        super(name, optional);
        this.fields = fields;
        this.type = type;
    }

    public String getTypeName() {
        return type.getName();
    }

    public boolean usesPresenceMapBit() {
        return false;
    }

    public org.lasalletech.entity.Type getType() {
        return type;
    }

    public Scalar[] getFields() {
        return fields;
    }

    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null || !obj.getClass().equals(ComposedScalar.class))
            return false;
        ComposedScalar other = (ComposedScalar) obj;
        if (other.fields.length != fields.length)
            return false;
        if (!other.getName().equals(getName()))
            return false;
        for (int i = 0; i < fields.length; i++) {
            if (!other.fields[i].getType().equals(fields[i].getType()))
                return false;
            if (!other.fields[i].getOperator().equals(fields[i].getOperator()))
                return false;
        }
        return true;
    }

    public int hashCode() {
        return name.hashCode();
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("Composed {");
        for (int i = 0; i < fields.length; i++)
            builder.append(fields[i].toString()).append(", ");
        builder.delete(builder.length() - 2, builder.length());
        return builder.append("}").toString();
    }
}