package org.openfast.simple;

import org.lasalletech.entity.EntityType;
import org.lasalletech.entity.QName;
import org.lasalletech.entity.simple.SimpleEntity;
import org.openfast.template.Composite;
import org.openfast.template.Group;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;

public abstract class SimpleComposite extends SimpleEntity implements Composite {
    public SimpleComposite(QName name) {
        super(name);
    }

    private String childNamespace;
    private QName typeReference;
    
    public QName getTypeReference() {
        return typeReference;
    }

    public boolean hasTypeReference() {
        return typeReference != null;
    }

    public Scalar getScalar(int index) {
        return (Scalar) getField(index);
    }

    public void setChildNamespace(String namespace) {
        this.childNamespace = namespace;
    }

    public String getChildNamespace() {
        return childNamespace;
    }

    public void setTypeReference(QName typeReference) {
        this.typeReference = typeReference;
    }

    public Group getGroup(int index) {
        return (Group) ((EntityType)getField(index).getType()).getEntity();
    }

    public Sequence getSequence(int index) {
        return (Sequence) ((EntityType)getField(index).getType()).getEntity();
    }
}
