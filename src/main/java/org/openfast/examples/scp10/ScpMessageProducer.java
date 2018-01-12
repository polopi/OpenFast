package org.openfast.examples.scp10;

import org.openfast.Global;
import org.openfast.Message;
import org.openfast.MessageOutputStream;
import org.openfast.error.ErrorHandler;
import org.openfast.examples.producer.XmlCompressedMessageConverter;
import org.openfast.session.*;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Registry;
import org.openfast.template.loader.XMLMessageTemplateLoader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class ScpMessageProducer implements MessageListener {
    private final Endpoint endpoint;
    private final Registry<MessageTemplate> templateRegistry;
    private String clientName = "LTG";

    public ScpMessageProducer(Endpoint endpoint, File templatesFile) {
        Global.setErrorHandler(ErrorHandler.NULL);
        this.endpoint = endpoint;
        XMLMessageTemplateLoader loader = new XMLMessageTemplateLoader();
        loader.setLoadTemplateIdFromAuxId(true);
        try {
            loader.load(new FileInputStream(templatesFile));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
        this.templateRegistry = loader.getTemplateRegistry();
    }

    public void encode(File xmlDataFile, MessageOutputStream out) throws FastConnectionException, IOException {
        XmlCompressedMessageConverter converter = new XmlCompressedMessageConverter();
        converter.setTemplateRegistry(templateRegistry);
        List messages = converter.parse(new FileInputStream(xmlDataFile));
        while (true) {
            for (int i=0; i<messages.size(); i++) {
                Message message = (Message) messages.get(i);
                out.writeMessage(message);
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
    }

    public void start(File xmlDataFile) throws FastConnectionException, IOException {
        FastClient client = new FastClient(clientName, SessionConstants.SCP_1_0, endpoint);
        client.setOutboundTemplateRegistry(templateRegistry);
        client.setInboundTemplateRegistry(templateRegistry);
        client.setMessageHandler(this);
        Session session = client.connect();
        encode(xmlDataFile, session.out);
    }

    public void stop() {
        endpoint.close();
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public void onMessage(Session session, Message message) {
        System.out.println("IN: " + message.toString());
    }
}

