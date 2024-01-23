class SnakeCell{
    SnakeCell nextSnakeCell = null;
    SnakeCell previousSnakeCell = null;
    int x ;
    int y ;
    void initSnakeCase(SnakeCell nextSnakeCell, SnakeCell previousSnakeCell, int x ,int y){
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
    void setX(int x){
        this.x=x;
    }
    void setY(int y){
        this.y=y;
    }
    SnakeCell getNext(){
        return this.nextSnakeCell;
    }

    SnakeCell getPrevious(){
        return this.previousSnakeCell;
    }

    void setNext(SnakeCell snakeCell){
        this.nextSnakeCell = snakeCell;
    }

    void setPrevious(SnakeCell snakeCell){
        this.previousSnakeCell=snakeCell;
    }
}
class Snake{
    protected SnakeCell head;
    protected SnakeCell tail;
    protected int xPositionHead;
    protected int yPositionHead;
    protected int maxX;
    protected int maxY;
    protected Reward reward;
    protected int color;
    protected int colorReward;
    protected GameBoy gb;
    int VerticalDirection = 0;
    int HorizontalDirection = 1;
    int direction;

    int getDirection() {
        return direction;
    }

    void initSnake(int xH, int yH, int xMax, int yMax, int xR, int yR, GameBoy g, int color, int color2){
        this.gb = g;
        this.color = color;
        this.colorReward = color2;
        this.head = new SnakeCell();
        this.tail = new SnakeCell();
        this.xPositionHead=xH;
        this.yPositionHead=yH;
        this.maxX=xMax;
        this.maxY=yMax;
        this.head.initSnakeCase(null,this.tail,xH,yH);
        this.tail.initSnakeCase(this.head,null,xH-1,yH);
        this.direction = this.VerticalDirection;
        this.reward=new Reward();
        this.reward.initReward(xR, yR,xMax,yMax);
        this.gb.setColor(color, xH, yH);
        this.gb.setColor(color, xH-1, yH);
        this.gb.setColor(colorReward, xR, yR);
    }

    int getHeadX(){
        return xPositionHead;
    }

    int getHeadY(){
        return yPositionHead;
    }


    boolean move(int i){
            boolean a;
            int backgroundColor = gb.getBackgroundColor();
            SnakeCell newHead;
            if(i==0){
                goUp();
            }
            else if (i==1){
                goDown();
            }
            else if(i==2){
                goRight();
            }
            else if(i==3){
                goLeft();
                }
            a = this.collusion();
            gb.setColor(this.color, this.xPositionHead, this.yPositionHead);
            //si la position du head est celle de la reward
            if(xPositionHead==this.reward.x && yPositionHead==this.reward.y){
                newHead=new SnakeCell();
                newHead.initSnakeCase(null, null,xPositionHead,yPositionHead);
                this.head.setNext(newHead);
                newHead.setPrevious(this.head);
                this.head=newHead;
                this.head.setNext(null);
                this.reward.changeParams();
                gb.setColor(this.colorReward, this.reward.getX(), this.reward.getY());
            }
            else{
                gb.setColor(backgroundColor, this.tail.getX(), this.tail.getY());
                newHead = this.tail;
                this.tail = this.tail.getNext();
                this.head.setNext(newHead);
                newHead.setPrevious(this.head);
                this.head = newHead ;
                this.head.setNext(null);
                this.tail.setPrevious(null);
                this.head.setX(this.xPositionHead);
                this.head.setY(this.yPositionHead);

            }
            return a;
    }

    // Le modulo c'est pour que si on sort d'un coté (exp le coté droit) on sort depuis l'autre coté ( coté gauche)
    void goUp(){
        this.direction = this.VerticalDirection;
        this.yPositionHead = (this.yPositionHead - 1 + maxY)% maxY;
    }
    void goDown(){
        this.direction = this.VerticalDirection;
        this.yPositionHead = (this.yPositionHead + 1) %maxY;
    }
     void goRight(){
        this.direction = this.HorizontalDirection;
        this.xPositionHead = (this.xPositionHead +1 ) % maxX;
     }
     void goLeft(){
        this.direction = this.HorizontalDirection;
        this.xPositionHead = (this.xPositionHead -1 + maxX) %maxX;
     }
     boolean collusion(){
        SnakeCell current = this.tail;
        while(current != null){
            if(current.getX() == this.xPositionHead && current.getY() == this.yPositionHead){
                return false;
            }
            current = current.getNext();
        }
        return true ;
     }

     void printSnake(){
        SnakeCell current = this.head;
        while (current != null) {
            print(current.getX());
            println(current.getY());
            current = current.getPrevious();
        }
     }
}
class Reward {
    int x ;
    int y ;
    int maxX = 20;
    int maxY = 18;
    // ces params pour la fonction random
    int s = 5;
    int a = 166;
    int c = 51 ;
    int m = 532 ;

    int getX(){
        return this.x;
    }

    int getY(){
        return this.y;
    }

    void initReward(int x,int y, int maxX , int maxY){
        this.x = x;
        this.y = y;
        this.maxX = maxX;
        this.maxY = maxY;
    }
    void changeParams(){
        this.x = ((this.a* this.x +this.c)% m)%100 % this.maxX;
        this.y = (((this.a*s+ this.y + this.c)% this.m)%100 -10) % this.maxY ;
//        print("reward :");
//        print(this.x);
//        print("-");
//        println(this.y);
    }
}

