package org.openfast;

import org.openfast.error.ErrorHandler;
import org.openfast.logging.FastMessageLogger;
import org.openfast.template.Define;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Registry;

public class NullOpenFastContext implements OpenFastContext {

    public MessageTemplate getTemplate(int templateId) {
        return null;
    }

    public int getTemplateId(MessageTemplate template) {
        return 0;
    }

    public Registry<MessageTemplate> getTemplateRegistry() {
        return null;
    }

    public Registry<Define> getDefineRegistry() {
        return null;
    }

    public void registerTemplate(int templateId, MessageTemplate template) {
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
    }

    public void setTemplateRegistry(Registry<MessageTemplate> registry) {
    }

    public void setDefineRegistry(Registry<Define> registry) {

    }

    public FastMessageLogger getLogger() {
        return FastMessageLogger.NULL;
    }

    public void setLogger(FastMessageLogger logger) {
    }

}
