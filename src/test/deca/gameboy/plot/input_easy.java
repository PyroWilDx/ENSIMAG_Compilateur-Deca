#include "GameBoy.java"
#include "Ball.java"

{
    boolean updated = false;
    int x;
    int y;
    int vitx;
    int vity;
    Ball ball = new Ball();

    GameBoy gb = new GameBoy();
    gb.init();
    x = 8;
    y = 10;
    vitx = 0;
    vity = 0;

    ball.init(gb, x, y, vitx, vity, gb.LIGHT);
    //gb.setTile(125, 1, 1);
    gb.setBackgroundColor(gb.DARK);
    while(true) {
        if (gb.keyPressed(gb.UP_KEY)) {
            ball.setVity(1);
            ball.setDown(false);
        }
        if (gb.keyPressed(gb.DOWN_KEY)) {
            ball.setVity(1);
            ball.setDown(true);
        }
        if (gb.keyPressed(gb.LEFT_KEY)) {
            ball.setVitx(1);
            ball.setForward(false);
        }
        if (gb.keyPressed(gb.RIGHT_KEY)) {
            ball.setVitx(1);
            ball.setForward(true);
        }
        updated = gb.updateScreen();
        if (updated) {
            gb.sleep(1);
            ball.moveAndDraw();
            ball.setVitx(0);
            ball.setVity(0);
        }
    }
}