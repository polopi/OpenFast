package org.openfast.util;



public class SimpleIntegerMap<E> implements IntegerMap<E> {
    private static final int DEFAULT_INC_SIZE = 32;
    private E[] table;
    private int firstKey;
    private final int incSize;
    
    public SimpleIntegerMap() {
        this(DEFAULT_INC_SIZE);
    }
    
    public SimpleIntegerMap(int size) {
        this.incSize = size;
    }
    
    @Override
	public void put(int key, E object) {
        adjust(key);
        table[key - firstKey] = object;
    }

    @SuppressWarnings("unchecked")
	private void adjust(int key) {
        if (table == null) {
            table = (E[])new Object[incSize];
            firstKey = key;
        } else if (firstKey > key) {
            E[] originalTable = table;
            int diff = firstKey - key;
            table = (E[])new Object[originalTable.length + diff];
            System.arraycopy(originalTable, 0, table, diff, originalTable.length);
            firstKey = key;
        } else if (key >= firstKey + table.length) {
            Object[] originalTable = table;
            int diff = key - (firstKey + table.length);
            table = (E[])new Object[originalTable.length + diff + incSize];
            System.arraycopy(originalTable, 0, table, 0, originalTable.length);
        }
    }

    @Override
	public E get(int key) {
        if (undefined(key))
            return null;
        return table[key-firstKey];
    }

    private boolean undefined(int key) {
        return table == null || key < firstKey || key >= table.length + firstKey;
    }

    @Override
	public boolean containsKey(int key) {
        if (undefined(key))
            return false;
        return table[key-firstKey] != null;
    }

    @Override
	public E remove(int key) {
        if (undefined(key))
            return null;
        E removed = table[key-firstKey];
        table[key-firstKey] = null;
        return removed;
    }
}
