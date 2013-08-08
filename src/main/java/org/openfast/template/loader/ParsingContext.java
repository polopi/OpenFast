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
package org.openfast.template.loader;

import java.util.List;
import java.util.Map;

import org.openfast.QName;
import org.openfast.error.ErrorHandler;
import org.openfast.template.TemplateRegistry;
import org.openfast.template.type.Type;
import org.w3c.dom.Element;

public class ParsingContext {
    static final ParsingContext NULL = new ParsingContext();
    static {
        NULL.setDictionary("global");
        NULL.setNamespace("");
        NULL.setTemplateNamespace("");
    }
    private final ParsingContext parent;
    private String templateNamespace = null;
    private String namespace = null;
    private String dictionary = null;
    private ErrorHandler errorHandler;
    private TemplateRegistry templateRegistry;
    private Map<String, Type> typeMap;
    private List<FieldParser> fieldParsers;
    private QName name;

    public ParsingContext() {
        this(NULL);
    }

    public ParsingContext(ParsingContext parent) {
        this.parent = parent;
    }

    public ParsingContext(Element node, ParsingContext parent) {
        this.parent = parent;
        if (node.hasAttribute("templateNs"))
            setTemplateNamespace(node.getAttribute("templateNs"));
        if (node.hasAttribute("ns"))
            setNamespace(node.getAttribute("ns"));
        if (node.hasAttribute("dictionary"))
            setDictionary(node.getAttribute("dictionary"));
        if (node.hasAttribute("name"))
            setName(new QName(node.getAttribute("name"), getNamespace()));
    }

    private void setName(QName name) {
        this.name = name;
    }

    public void setTemplateNamespace(String templateNS) {
        this.templateNamespace = templateNS;
    }

    public String getTemplateNamespace() {
        return (templateNamespace != null) ? templateNamespace : parent.getTemplateNamespace();
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getNamespace() {
        return (namespace != null) ? namespace : parent.getNamespace();
    }

    public void setDictionary(String dictionary) {
        this.dictionary = dictionary;
    }

    public String getDictionary() {
        return (dictionary != null) ? dictionary : parent.getDictionary();
    }

    public void setErrorHandler(ErrorHandler errorHandler) {
        this.errorHandler = errorHandler;
    }

    public ErrorHandler getErrorHandler() {
        return (errorHandler != null) ? errorHandler : parent.getErrorHandler();
    }

    public TemplateRegistry getTemplateRegistry() {
        return (templateRegistry != null) ? templateRegistry : parent.getTemplateRegistry();
    }

    public void setTemplateRegistry(TemplateRegistry templateRegistry) {
        this.templateRegistry = templateRegistry;
    }

    public void setTypeMap(Map<String, Type> typeMap) {
        this.typeMap = typeMap;
    }

    public Map<String, Type> getTypeMap() {
        return (typeMap != null) ? typeMap : parent.getTypeMap();
    }

    public List<FieldParser> getFieldParsers() {
        return (fieldParsers != null) ? fieldParsers : parent.getFieldParsers();
    }

    public void setFieldParsers(List<FieldParser> list) {
        this.fieldParsers = list;
    }

    public FieldParser getFieldParser(Element element) {
        List<FieldParser> parsers = getFieldParsers();
        for (int i = parsers.size() - 1; i >= 0; --i) {
            FieldParser fieldParser = parsers.get(i);
            if (fieldParser.canParse(element, this))
                return fieldParser;
        }
        return null;
    }

    public ParsingContext getParent() {
        return parent;
    }

    public QName getName() {
        return name;
    }

    public void addFieldParser(FieldParser parser) {
        getFieldParsers().add(parser);
    }
}
