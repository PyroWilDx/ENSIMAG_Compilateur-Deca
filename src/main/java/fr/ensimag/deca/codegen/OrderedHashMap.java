package fr.ensimag.deca.codegen;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class OrderedHashMap<K, V> extends HashMap<K, V> {
    private static final long serialVersionUID = 5554277472235530940L;
    private final LinkedList<K> linkedList;

    public OrderedHashMap() {
        super();

        linkedList = new LinkedList<>();
    }

    public void addFirst(K key, V value) {
        if (!containsKey(key)) {
            put(key, value);
            linkedList.addFirst(key);
        }
    }

    public void addLast(K key, V value) {
        if (!containsKey(key)) {
            put(key, value);
            linkedList.addLast(key);
        }
    }

    public void updateValue(K key, V value) {
        if (containsKey(key)) {
            put(key, value);
        }
    }

    public LinkedList<K> getOrderedLinkedList() {
        return linkedList;
    }
}
