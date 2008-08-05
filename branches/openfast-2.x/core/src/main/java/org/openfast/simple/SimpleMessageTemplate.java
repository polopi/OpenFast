package org.openfast.simple;

import org.lasalletech.entity.EObject;
import org.lasalletech.entity.QName;
import org.openfast.Message;
import org.openfast.template.MessageTemplate;

public class SimpleMessageTemplate extends SimpleComposite implements MessageTemplate {
    public SimpleMessageTemplate(QName name) {
        super(name);
    }
    
    public SimpleMessageTemplate(String name) {
        super(new QName(name));
    }

    public EObject newObject() {
        return newMessage();
    }
    
    public Message newMessage() {
        return new SimpleMessage(this);
    }
}
