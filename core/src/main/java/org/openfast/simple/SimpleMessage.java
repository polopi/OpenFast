package org.openfast.simple;

import org.lasalletech.entity.simple.SimpleEObject;
import org.openfast.Message;
import org.openfast.template.MessageTemplate;


public class SimpleMessage extends SimpleEObject implements Message {

    protected SimpleMessage(MessageTemplate template) {
        super(template);
    }

    public MessageTemplate getTemplate() {
        return (MessageTemplate) getEntity();
    }
}
