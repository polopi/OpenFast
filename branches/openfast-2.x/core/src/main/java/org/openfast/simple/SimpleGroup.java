package org.openfast.simple;

import org.lasalletech.entity.EObject;
import org.lasalletech.entity.QName;
import org.openfast.template.Group;

public class SimpleGroup extends SimpleComposite implements Group {

    public SimpleGroup(QName name) {
        super(name);
    }

    public EObject newObject() {
        return new SimpleFastObject(this);
    }
}
