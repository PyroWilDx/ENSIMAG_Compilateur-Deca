Class Events {
    private Event nextEvent;
    private Event previousEvent;
    public Events(Event nextEvent,Event previousEvent){
        this.nextEvent = nextEvent;
        this.previousEvent = previousEvent;

    }
    public Event getNext(){
        return this.nextEvent;
    }

    public Event getPrevious(){
        return this.previousEvent;
    }

    public void setNext(Event event){
        this.nextEvent = event;
    }

    public void setPrevious(Event event){
        this.previoustEvent = event;
    }

        }