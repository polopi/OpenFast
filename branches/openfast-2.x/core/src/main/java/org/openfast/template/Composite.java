package org.openfast.template;

import org.lasalletech.entity.Entity;
import org.lasalletech.entity.QName;

public interface Composite extends Entity {
    Scalar getScalar(int index);
    Sequence getSequence(int index);
    Group getGroup(int index);
    void setChildNamespace(String namespace);
    void setTypeReference(QName typeReference);
    boolean hasTypeReference();
    QName getTypeReference();
}
