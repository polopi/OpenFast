package org.openfast.template;

import org.openfast.QName;

import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class NullRegistry<T extends Field> implements Registry<T> {
    public void registerAll(Registry registry) {

    }

    public void register(int id, T element) {

    }

    public void register(int id, String name) {

    }

    public void register(int id, QName name) {

    }

    public void define(T element) {

    }

    public void remove(String name) {

    }

    public void remove(QName name) {

    }

    public void remove(T template) {

    }

    public void remove(int id) {

    }

    public T get(int id) {
        return null;
    }

    public T get(String name) {
        return null;
    }

    public T get(QName name) {
        return null;
    }

    public List<T> getAll() {
        return Collections.emptyList();
    }

    public int getId(String name) {
        return 0;
    }

    public int getId(QName name) {
        return 0;
    }

    public int getId(T template) {
        return 0;
    }

    public boolean isRegistered(String name) {
        return false;
    }

    public boolean isRegistered(QName name) {
        return false;
    }

    public boolean isRegistered(int id) {
        return false;
    }

    public boolean isRegistered(T template) {
        return false;
    }

    public boolean isDefined(String name) {
        return false;
    }

    public boolean isDefined(QName name) {
        return false;
    }

    public void addRegisteredListener(RegisteredListener<T> registeredListener) {

    }

    public void removeRegisteredListener(RegisteredListener<T> registeredListener) {

    }

    @SuppressWarnings("unchecked")
    public Iterator<QName> nameIterator() {
        return (Iterator<QName>) Collections.EMPTY_LIST.iterator();
    }

    @SuppressWarnings("unchecked")
    public Iterator<T> iterator() {
        return (Iterator<T>) Collections.EMPTY_LIST.iterator();
    }
}
