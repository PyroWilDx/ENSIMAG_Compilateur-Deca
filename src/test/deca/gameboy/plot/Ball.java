class Ball {
    protected int x;
    protected int y;
    protected GameBoy gb;
    protected boolean down = true;
    protected boolean forward = true;
    protected int color;
    protected int vitx;
    protected int vity;

    void init(GameBoy g, int x, int y, int vitx, int vity, int color) {
        this.x = x;
        this.y = y;
        this.gb = g;
        this.vitx = vitx;
        this.vity = vity;
        this.color = color;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    void moveAndDraw() {
        int backgroundColor = gb.getBackgroundColor();
        int oldX;
        int oldY;
        if (x <= vitx || x >= gb.WIDTH - 1) {
            forward = !forward;
        }
        if (y <= vity || y >= gb.HEIGHT - 1) {
            down = !down;
        }
        oldX = x;
        oldY = y;

        if (forward) {
            x = x + vitx;
        } else {
            x = x - vitx;
        }
        if (!down) {
            y = y + vity;
        } else {
            y = y - vity;
        }
        //gb.setColor(gb.WHITE, oldX, oldY);
        gb.setColor(backgroundColor, oldX, oldY);
        //gb.setColor(gb.BLACK, x, y);
        gb.setColor(this.color, x, y);
    }

    void setX(int x) {
        this.x = x;
    }

    void setY(int y) {
        this.y = y;
    }

    void setColor(int color) {
        this.color = color;
    }

    void setVitx(int vitx) {
        this.vitx = vitx;
    }

    void setVity(int vity) {
        this.vity = vity;
    }
}