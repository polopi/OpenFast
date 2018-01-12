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
package org.openfast.template;

import org.openfast.QName;
import org.openfast.util.IntegerMap;
import org.openfast.util.SimpleIntegerMap;

import java.util.*;

public class BasicRegistry<T extends Field> extends AbstractRegistry<T> {
    private final Map<QName, T> nameMap = new HashMap<QName, T>();
    private final IntegerMap idMap = new SimpleIntegerMap();
    private final Map<T, Integer> elementMap = new HashMap<T, Integer>();
    private final List<T> elements = new ArrayList<T>();

    public void register(int id, T element) {
        define(element);
        Integer tid = id;
        idMap.put(id, element);
        elementMap.put(element, tid);
        notifyRegistered(element, id);
    }
    public void register(int id, QName name) {
        if (!nameMap.containsKey(name))
            throw new IllegalArgumentException("The element named " + name + " is not defined.");
        Integer tid = id;
        T template = nameMap.get(name);
        elementMap.put(template, tid);
        idMap.put(id, template);
        notifyRegistered(template, id);
    }
    public void define(T element) {
        if (!elements.contains(element)) {
            nameMap.put(element.getQName(), element);
            elements.add(element);
        }
    }
    public int getId(QName name) {
        T element = nameMap.get(name);
        if (element == null || !elementMap.containsKey(element))
            return -1;
        return elementMap.get(element);
    }
    public T get(int templateId) {
        return (T) idMap.get(templateId);
    }
    public T get(QName name) {
        return nameMap.get(name);
    }
    public int getId(T template) {
        if (!isRegistered(template))
            return -1;
        return elementMap.get(template);
    }
    public boolean isRegistered(QName name) {
        return nameMap.containsKey(name);
    }
    public boolean isRegistered(int templateId) {
        return idMap.containsKey(templateId);
    }
    public boolean isRegistered(T template) {
        return elementMap.containsKey(template);
    }
    public boolean isDefined(QName name) {
        return nameMap.containsKey(name);
    }
    public List<T> getAll() {
        return elements;
    }
    public void remove(QName name) {
        T template = (T) nameMap.remove(name);
        Object id = elementMap.remove(template);
        idMap.remove(((Integer) id).intValue());
        elements.remove(template);
    }
    public void remove(T template) {
        Object id = elementMap.remove(template);
        nameMap.remove(template.getName());
        idMap.remove(((Integer)id).intValue());
    }
    public void remove(int id) {
        T template = (T) idMap.remove(id);
        elementMap.remove(template);
        nameMap.remove(template.getName());
    }
    public void registerAll(Registry<T> registry) {
        if (registry == null) return;
        List<T> templates = registry.getAll();
        if (templates == null) return;
        for (T template : templates) {
            register(registry.getId(template), template);
        }
    }
    public Iterator<QName> nameIterator() {
        return nameMap.keySet().iterator();
    }
    public Iterator<T> iterator() {
        return elements.iterator();
    }
}
