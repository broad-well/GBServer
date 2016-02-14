package com.Gbserver.variables;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by michael on 11/8/15.
 */
public class EnhancedMap<T> extends LinkedList<EnhancedMap.Entry> {
    public class Entry<T> {
        private T val1;
        private T val2;

        Entry(T arg1, T arg2){
            val1 = arg1;
            val2 = arg2;
        }

        public T getFirst() {
            return val1;
        }

        public T getSecond() {
            return val2;
        }
    }

    public EnhancedMap() {
        super();
    }

    public boolean put(T arg1, T arg2){
        if(has(arg1) || has(arg2)) return false;
        add(new Entry<>(arg1, arg2));
        return true;
    }

    public Object get(T object){
        if(!has(object)) return null;
        for(EnhancedMap.Entry entry : this){
            if(entry.getFirst().equals(object)) return entry.getSecond();
            if(entry.getSecond().equals(object)) return entry.getFirst();
        }
        return null; //Should not reach this point.
    }

    public boolean has(T object){
        for(EnhancedMap.Entry entry : this){
            if(entry.getFirst().equals(object) || entry.getSecond().equals(object)) return true;
        }
        return false;
    }

    public boolean delete(T arg){
        if(!has(arg)) return false;
        for(EnhancedMap.Entry entry : this){
            if(entry.getFirst().equals(arg) || entry.getSecond().equals(arg)){
                remove(entry);
                return true;
            }
        }
        return false; //The program should not reach here.
    }

    private List<Object> getContent() {
        LinkedList<Object> list = new LinkedList<>();
        for(EnhancedMap.Entry entry : this){
            list.add(entry.getFirst());
            list.add(entry.getSecond());
        }
        return list;
    }

    @Override
    public String toString() {
        String out = "";
        for(EnhancedMap.Entry entry : this){
            out += "[" + entry.getFirst().toString() + ", " + entry.getSecond().toString() + "] ";
        }
        return out;
    }
}
