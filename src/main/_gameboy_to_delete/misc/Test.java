class Event {
    Event nextEvent;
    Event previousEvent;
    int i;
    void initEvent(Event nextEvent,Event previousEvent ,int i){
        this.nextEvent = nextEvent;
        this.previousEvent = previousEvent;
        this.i = i;
    }
    Event getNext(){
        return this.nextEvent;
    }

    Event getPrevious(){
        return this.previousEvent;
    }

    void setNext(Event event){
        this.nextEvent = event;
    }

    void setPrevious(Event event){
        this.previousEvent = event;
    }
    int Execute(){
        // je c pas ce que doit faire chaque evenement
        return 1;
    }
}
//------------------------------------------------
class DoubleLinkedListEvents {
    Event head=null;
    Event tail=null;

    void addEvent(Event event) {
        if (head == null) {
            head = event;
            tail = event;
        } else {
            tail.setNext(event);
            event.setPrevious(tail);
            tail = event;
        }
    }
    void removeEvent(Event event) {
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

    Event getTail() {
        return tail;
    }

    Event getHead() {
        return head;
    }

    Event popLast(){
        Event a = tail;
        removeEvent(tail);
        return a;

    }

    Event popFirst(){
        return head;
        removeEvent(head);
    }
    void displayEvents() {
        Event current = head;
        while (current != null) {
            println(current.i);
            current = current.getNext();
        }
    }
}
//---------------------------------------------------------------
//#include "DoubleLinkedList.decah"
class EventsManager{
    DoubleLinkedList events;

    void EventsManager(){
        this.events = new DoubleLinkedList();
    }

    Event getFirst(){
        events.getHead();
    }

    Event popFirst(){
        return events.popFirst();
    }

    void addLast(Event event){
        events.addEvent(event);
    }

    int run(){
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
//----------------------------------------------------
class SnakeCell{
    SnakeCell nextSnakeCell = null;
    SnakeCell previousSnakeCell = null;
    int x ;
    int y ;
    SnakeCase(SnakeCell nextSnakeCell, SnakeCell previousSnakeCell, int x ,int y){
        this.nextSnakeCell = nextSnakeCell;
        this.previousSnakeCell = previousSnakeCell;
        this.x = x;
        this.y = y;
    }
    int getX(){
        return this.x;
    }
    int getY(){
        return this.y;
    }
    Event getNext(){
        return this.nextSnakeCell;
    }

    Event getPrevious(){
        return this.previousSnakeCell;
    }

    void setNext(SnakeCell snakeCell){
        this.nextSnakeCell = snakeCell;
    }

    void setPrevious(SnakeCell snakeCell){
        this.previousSnakeCell=snakeCell;
    }


}
//----------------------------------------------------
class Snake{
    protected SnakeCell head;
    protected SnakeCell tail;
    protected int xPositionHead;
    protected int yPositionHead;
    protected int maxX;
    protected int maxY;

    void Snake(int x,int y){
        this.head = new SnakeCell(null,null,x,y);
        this.tail = this.head;
        this.xPositionHead=x;
        this.yPositionHead=y;
    }
    //TODO c'est mouvement depends de l'orientation du repaire

    boolean move(int i){//   il manque l'input comme argument
        //j'ai besoin de l'input pour decider dans quel if dans le else je vais entrer
        //depends on the input i chose where to go and update the new xPositionHead and the yPositionHead
        if(collusion(this.x,this.y)){
            Println("FIN DU JEU");
            return false;
        }
        else{
            if(i==0){
                goUp();
                print(this.xPositionHead);
                println(this.yPositionHead);
            }
            else if (i==1){
                goDown();
                print(this.xPositionHead);
                println(this.yPositionHead);
            }
            else if(i==2){
                goRight();
                print(this.xPositionHead);
                println(this.yPositionHead);
            }
            elseif(i==3){
                goLeft();
                print(this.xPositionHead);
                println(this.yPositionHead);
                }
            /*SnakeCell newHead = new SnakeCell(null,this.head,this.xPositionHead,this.yPositionHead);
            this.head.setNext(newHead);
            this.head = newHead;
            this.tail = this.tail.nextSnakeCell;*/
            SnakeCell newHead = this.tail;
            this.tail = this.tail.getNext();//it get null if only one
            this.head.setNext(newHead);
            newHead.setPrevious(this.head);
            this.head = newHead ;
            this.head.setNext(null);
            //ici je dois ajouter si la position du head est une reward je met le tail en tail.previous sinon
            // son previous devient null;
        }
    }
    void goUp(){
        this.yPositionHead = this.yPositionHead - 1;
    }
    void goDown(){
        this.yPositionHead = this.yPositionHead + 1;
    }
     void goRight(){
        this.xPositionHead = this.xPositionHead +1 ;
     }
     void goLeft(){
        this.xPositionHead = this.xPositionHead -1;
     }
     boolean collusion(){
        SnakeCell current = this.tail;
        boolean a = false;
        if (this.yPositionHead >this.maxY || this.yPositionHead <= 0 || this.xPositionHead <= 0 || this.yPositionHead > this.maxX){
            a = true;
        }
        else{
            while(current != null){
                if(current.getX() == this.xPositionHead && current.getY() == this.yPositionHead){
                    a = true;
                }
                current = current.getNext();
            }
        }
        return a;
     }
}
//---------------------------------------------------
class Reward{
    int x ;
    int y ;
    int maxX;
    int maxY;
    // ces params pour la fonction random
    int s = 5;
    int a = 166;
    int c = 51 ;
    int m = 532 ;

    void initReward(int x,int y){
        this.x = x;
        this.y = y;
    }
    void changeParams(){
        this.x = ((this.a* this.x +this.c)% m)%100 % this.maxX;
        this.y = (((this.a*s+ this.y + this.c)% this.m)%100 -10) % this.maxY ;
    }
}
//----------------------------------------------------
class Test{
    static void main(String[] args) {
        EventsManager events = new EventsManager();
        //on a besion d'une interface graphique
        boolean running = true;
        while(running){
            event = getInput() ;//  FAUX : faux pas attendre l'input mais faux continuer si il n y'a pas de input
            events.addLast(event);
            Event current = events.getHead();
            while(current != null && running){
                running = current.execute() ;
                this.events.popFirst();
                current = events.getHead();
            }
            //on execute ce qui doit être executer


        }
    }
        }