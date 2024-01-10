package fr.ensimag.deca.codegen;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;

public class OrderedHashSet<E> extends HashSet<E> {
    private static final long serialVersionUID = 6141682385710520966L;
    private final LinkedList<E> linkedList;

    public OrderedHashSet() {
        linkedList = new LinkedList<>();
    }

    public void addFirst(E e) {
        if (!contains(e)) {
            add(e);
            linkedList.addFirst(e);
        }
    }

    public void addLast(E e) {
        if (!contains(e)) {
            add(e);
            linkedList.addLast(e);
        }
    }

    @Override
    public Iterator<E> iterator() {
        return linkedList.iterator();
    }

}
