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


package org.openfast;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.openfast.codec.FastEncoder;
import org.openfast.error.FastConstants;
import org.openfast.session.Session;
import org.openfast.template.MessageTemplate;
import org.openfast.template.TemplateRegisteredListener;
import org.openfast.template.TemplateRegistry;


public class MessageOutputStream implements MessageStream {
    private final OutputStream out;
    private final FastEncoder encoder;
    private final Context context;
    private Map handlers = Collections.EMPTY_MAP;

    public MessageOutputStream(OutputStream outputStream) {
        this(outputStream, new Context());
    }

    public MessageOutputStream(OutputStream outputStream, Context context) {
        this.out = outputStream;
        this.encoder = new FastEncoder(context);
        this.context = context;
    }
    
    public void writeMessage(Message message) {
    	writeMessage(message, false);
    }

    public void writeMessage(Message message, boolean flush) {
        try {
            if (handlers.containsKey(message.getTemplate())) {
                ((MessageHandler) handlers.get(message.getTemplate())).handleMessage(message,
                    context, encoder);
            }

            byte[] data = encoder.encode(message);

            if ((data == null) || (data.length == 0)) {
                return;
            }

            out.write(data);
            if (flush)
            	out.flush();
        } catch (IOException e) {
        	Global.handleError(FastConstants.IO_ERROR, "An IO error occurred while writing message " + message, e);
        }
    }

    public void reset() {
        writeMessage(Session.RESET);
        encoder.reset();
    }

    public void registerTemplate(int templateId, MessageTemplate template) {
        encoder.registerTemplate(templateId, template);
    }

    public void close() {
        try {
            out.close();
        } catch (IOException e) {
            Global.handleError(FastConstants.IO_ERROR, "An error occurred while closing output stream.", e);
        }
    }

    public OutputStream getUnderlyingStream() {
        return out;
    }

    public void addMessageHandler(MessageTemplate template, MessageHandler handler) {
        if (handlers == Collections.EMPTY_MAP) {
            handlers = new HashMap();
        }

        handlers.put(template, handler);
    }

	public void setTemplateRegistry(TemplateRegistry registry) {
		context.setTemplateRegistry(registry);
	}

	public MessageTemplate get(int templateId) {
		return context.get(templateId);
	}

	public MessageTemplate get(String templateName) {
		return context.get(templateName);
	}

	public int getTemplateId(String templateName) {
		return context.getTemplateId(templateName);
	}

	public int getTemplateId(MessageTemplate template) {
		return context.getTemplateId(template);
	}

	public boolean isRegistered(String templateName) {
		return context.isRegistered(templateName);
	}

	public boolean isRegistered(int templateId) {
		return context.isRegistered(templateId);
	}

	public boolean isRegistered(MessageTemplate template) {
		return context.isRegistered(template);
	}

	public MessageTemplate[] getTemplates() {
		return context.getTemplates();
	}

	public void addTemplateRegisteredListener(TemplateRegisteredListener templateRegisteredListener) {
		context.addTemplateRegisteredListener(templateRegisteredListener);
	}

	public void removeTemplate(String name) {
		context.removeTemplate(name);
	}

	public void removeTemplate(MessageTemplate template) {
		context.removeTemplate(template);
	}

	public void removeTemplate(int id) {
		context.removeTemplate(id);
	}
}