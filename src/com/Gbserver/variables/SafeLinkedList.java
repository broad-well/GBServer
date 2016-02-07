package com.Gbserver.variables;

import java.util.LinkedList;


public class SafeLinkedList<T> extends LinkedList<T> {

    public SafeLinkedList() {
        super();
    }

    public boolean insert(T obj) {
        if (contains(obj)) return false;
        add(obj);
        return true;
    }

    public boolean has(Object obj) {
        for (T object : this) {
            if (object.equals(obj) || object == obj) return true;
        }
        return false;
    }

}
