package org.openfast.util;


public interface IntegerMap<E> {

    void put(int key, E value);

    E get(int key);

    boolean containsKey(int key);

    E remove(int key);
}
