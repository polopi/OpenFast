package org.openfast.examples.tmplexch;

import org.openfast.Context;
import org.openfast.Message;
import org.openfast.codec.FastEncoder;
import org.openfast.session.SessionConstants;
import org.openfast.template.BasicRegistry;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Registry;
import org.openfast.template.loader.XMLMessageTemplateLoader;

import java.io.*;
import java.util.List;

public class TemplateExchangeDefinitionEncoder {
    private final Registry<MessageTemplate> templateRegistry;
    private final OutputStream out;

    public TemplateExchangeDefinitionEncoder(File templatesFile, boolean namespaceAware) {
        this(templatesFile, namespaceAware, System.out);
    }
    public TemplateExchangeDefinitionEncoder(File templatesFile, boolean namespaceAware, OutputStream out) {
        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader(namespaceAware);
        loader.setLoadTemplateIdFromAuxId(true);
        try {
            loader.load(new FileInputStream(templatesFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        this.templateRegistry = loader.getTemplateRegistry();
        this.out = out;
    }

    public void start() throws IOException {
        Context context = new Context();
        BasicRegistry registry = new BasicRegistry<MessageTemplate>();
        SessionConstants.SCP_1_1.registerSessionTemplates(registry);
        registry.registerAll(templateRegistry);
        context.setTemplateRegistry(registry);
        FastEncoder encoder = new FastEncoder(context);
        List<MessageTemplate> templates = templateRegistry.getAll();
        for (MessageTemplate template : templates) {
            Message message = SessionConstants.SCP_1_1.createTemplateDefinitionMessage(template);
            out.write(encoder.encode(message));
        }
    }
}
