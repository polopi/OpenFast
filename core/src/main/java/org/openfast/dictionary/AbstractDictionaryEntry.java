package org.openfast.dictionary;

import org.lasalletech.exom.QName;

public abstract class AbstractDictionaryEntry {
    protected final QName key;
    protected DictionaryEntry next;
    protected boolean isNull;
    protected boolean isDefined;
    
    public AbstractDictionaryEntry(QName key) {
        if (key == null) throw new NullPointerException();
        this.key = key;
    }

    public DictionaryEntry getNext() {
        return next;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean isNull() {
        return isNull;
    }
    
    public boolean isDefined() {
        return isDefined;
    }

    public boolean matches(Object key) {
        return this.key.equals(key);
    }

    public void setNext(DictionaryEntry entry) {
        next = entry;
    }

    public QName getKey() {
        return key;
    }
    
    public void setNull() {
        this.isNull = true;
        isDefined = true;
    }

    public void reset() {
        isDefined = false;
        isNull = false;
    }
}
