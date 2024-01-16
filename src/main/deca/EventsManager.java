//#include "DoubleLinkedList.decah"
Class EventsManager{
    private DoubleLinkedList events;

    public EventsManager(){
        this.events = new DoubleLinkedList();
    }

    public Event getFirst(){
        events.getHead();
    }

    public Event popFirst(){
        return events.popFirst();
    }

    public void addLast(Event event){
        events.addEvent(event);
    }

    public int run(){
        // y a pas encore l'ajout des evenement au fur et à mesure avec l'execution du programme
        Event current = this.events.getHead();
        while(current != null){
            current.execute() ;
            this.events.popFirst();
            current = events.getHead();
        }

        return 1;
    }

}