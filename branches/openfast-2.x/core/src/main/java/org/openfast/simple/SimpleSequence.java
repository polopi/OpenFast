package org.openfast.simple;

import org.lasalletech.entity.EObject;
import org.lasalletech.entity.QName;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;

public class SimpleSequence extends SimpleComposite implements Sequence {
    private final Scalar length;

    public SimpleSequence(QName name, Scalar length) {
        super(name);
        this.length = length;
    }

    public EObject newObject() {
        return new SimpleFastObject(this);
    }

    public Scalar getLength() {
        return length;
    }
}
