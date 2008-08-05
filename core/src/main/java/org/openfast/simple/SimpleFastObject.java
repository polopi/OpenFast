package org.openfast.simple;

import org.lasalletech.entity.EObject;
import org.lasalletech.entity.Entity;
import org.lasalletech.entity.simple.SimpleEObject;

public class SimpleFastObject extends SimpleEObject implements EObject {
    protected SimpleFastObject(Entity composite) {
        super(composite);
    }
}
