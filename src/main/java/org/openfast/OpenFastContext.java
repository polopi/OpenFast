package org.openfast;

import org.openfast.error.ErrorHandler;
import org.openfast.logging.FastMessageLogger;
import org.openfast.template.Define;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Registry;

public interface OpenFastContext {
    public int getTemplateId(MessageTemplate template);

    public MessageTemplate getTemplate(int templateId);

    public void registerTemplate(int templateId, MessageTemplate template);

    public void setErrorHandler(ErrorHandler errorHandler);

    public Registry<MessageTemplate> getTemplateRegistry();

    public Registry<Define> getDefineRegistry();

    public void setTemplateRegistry(Registry<MessageTemplate> registry);

    public void setDefineRegistry(Registry<Define> registry);

    public FastMessageLogger getLogger();
    
    public void setLogger(FastMessageLogger logger);
}
