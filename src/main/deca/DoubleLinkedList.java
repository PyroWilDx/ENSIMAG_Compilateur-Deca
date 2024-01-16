class DoubleLinkedList {
    private Event head;
    private Event tail;

    public DoubleLinkedList() {
        this.head = null;
        this.tail = null;
    }
    public void addEvent(Event event) {
        if (head == null) {
            head = event;
            tail = event;
        } else {
            tail.setNext(event);
            event.setPrevious(tail);
            tail = event;
        }
    }
    public void removeEvent(Event event) {
        Event prev = event.getPrevious();
        Event next = event.getNext();
        if (prev != null) {
            prev.setNext(next);
        } else {
            tail = next;
        }
        if (next != null) {
            next.setPrevious(prev);
        } else {
            // Updating tail if removing the last element
            head = prev;
        }
    }

    public Event getTail() {
        return tail;
    }

    public Event getHead() {
        return head;
    }

    public Event popLast(){
        return tail;
        removeEvent(tail);
    }

    public Event popFirst(){
        return head;
        removeEvent(head);
    }
    public void displayEvents() {
        Event current = head;
        while (current != null) {
            System.out.println("Event: " + current);
            current = current.getNext();
        }
    }
}