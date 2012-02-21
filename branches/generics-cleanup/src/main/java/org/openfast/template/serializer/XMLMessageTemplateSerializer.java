package org.openfast.template.serializer;

import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openfast.template.Field;
import org.openfast.template.Group;
import org.openfast.template.MessageTemplate;
import org.openfast.template.Scalar;
import org.openfast.template.Sequence;
import org.openfast.util.XmlWriter;

public class XMLMessageTemplateSerializer implements MessageTemplateSerializer {
    private SerializingContext initialContext;
    public XMLMessageTemplateSerializer() {
        this.initialContext = createInitialContext();
    }
    public static SerializingContext createInitialContext() {
        SerializerRegistry registry = new SerializerRegistry();
        registry.addFieldSerializer(new ScalarSerializer());
        registry.addFieldSerializer(new DynamicTemplateReferenceSerializer());
        registry.addFieldSerializer(new StaticTemplateReferenceSerializer());
        registry.addFieldSerializer(new ComposedDecimalSerializer());
        registry.addFieldSerializer(new GroupSerializer());
        registry.addFieldSerializer(new SequenceSerializer());
        registry.addFieldSerializer(new TemplateSerializer());
        registry.addFieldSerializer(new VariableLengthScalarSerializer());
        SerializingContext context = SerializingContext.createInitialContext(registry);
        return context;
    }
    public void serialize(MessageTemplate[] templates, OutputStream destination) {
        XmlWriter writer = new XmlWriter(destination);
        writer.setEnableProcessingInstructions(true);
        SerializingContext context = new SerializingContext(initialContext);
        writer.start("templates");
        String templateNamespace = whichTemplateNamespaceIsUsedMost(templates);
        String childNamespace = whichNamespaceIsUsedMode(templates);
        if (!"".equals(childNamespace))
            writer.addAttribute("ns", childNamespace);
        if (!"".equals(templateNamespace))
            writer.addAttribute("templateNs", templateNamespace);
        writer.addAttribute("xmlns", "http://www.fixprotocol.org/ns/fast/td/1.1");
        context.setTemplateNamespace(templateNamespace);
        context.setNamespace(childNamespace);
        for (MessageTemplate template : templates) {
            context.serialize(writer, template);
        }
        writer.end();
    }
    
    private String whichNamespaceIsUsedMode(MessageTemplate[] templates) {
        Map<String, Integer> namespaces = new HashMap<String, Integer>();
        for (MessageTemplate template : templates) {
            tallyNamespaceReferences(template, namespaces);
        }
        
        Iterator<String> iter = namespaces.keySet().iterator();
        int champion = 0;
        String championNs = "";
        while (iter.hasNext()) {
            String contender = iter.next();
            int contenderCount = namespaces.get(contender).intValue();
            if (contenderCount > champion) {
                champion = contenderCount;
                championNs = contender;
            }
        }
        return championNs;
    }
    private void tallyNamespaceReferences(Group group, Map<String, Integer> namespaces) {
        int start = 0;
        if (group instanceof MessageTemplate)
            start = 1;
        
        for (int i=start; i<group.getFieldCount(); ++i) {
        	Field groupField = group.getField(i);
            if (groupField instanceof Scalar) {
                String ns = groupField.getQName().getNamespace();
                namespaces.put(ns, namespaces.containsKey(ns) ?
                		new Integer(namespaces.get(ns).intValue() + 1) : new Integer(1) ); 
            } else if (groupField instanceof Group) {
                tallyNamespaceReferences((Group) groupField, namespaces);
            } else if (groupField instanceof Sequence) {
                tallyNamespaceReferences(((Sequence) groupField).getGroup(), namespaces);
            }
        }
    }
    
    private String whichTemplateNamespaceIsUsedMost(MessageTemplate[] templates) {
        Map<String, Integer> namespaces = new HashMap<String, Integer>();
        for (MessageTemplate template : templates) {
            String ns = template.getQName().getNamespace();
            namespaces.put(ns, namespaces.containsKey(ns) ? 
            	new Integer(namespaces.get(ns).intValue() + 1) : new Integer(1));
        }
        
        Iterator<String> iter = namespaces.keySet().iterator();
        int champion = 0;
        String championNs = "";
        while (iter.hasNext()) {
            String contender = iter.next();
            int contenderCount = namespaces.get(contender).intValue();
            if (contenderCount > champion) {
                champion = contenderCount;
                championNs = contender;
            }
        }
        return championNs;
    }
}
