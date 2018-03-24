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

import java.util.Iterator;
import java.util.List;

public interface Registry<T extends Field> {
    Registry NULL = new NullRegistry();

    void registerAll(Registry<T> registry);
    void register(int id, T element);
    void register(int id, String name);
    void register(int id, QName name);
    void define(T element);
    void remove(String name);
    void remove(QName name);
    void remove(T template);
    void remove(int id);
    T get(int id);
    T get(String name);
    T get(QName name);
    List<T> getAll();
    int getId(String name);
    int getId(QName name);
    int getId(T element);
    boolean isRegistered(String name);
    boolean isRegistered(QName name);
    boolean isRegistered(int id);
    boolean isRegistered(T element);
    boolean isDefined(String name);
    boolean isDefined(QName name);
    void addRegisteredListener(RegisteredListener<T> registeredListener);
    void removeRegisteredListener(RegisteredListener<T> registeredListener);
    /**
     * Iterator over the names of each element (defined or registered) in this
     * registry
     * 
     * @return an iterator over the qualified names each item is of type QName
     */
    Iterator<QName> nameIterator();
    /**
     * Iterator over the set of element (defined or registered) in this
     * registry
     * 
     * @return an iterator over the set of templates each item is an instance of
     *         MessageTemplate
     */
    Iterator<T> iterator();
}