class Color {
    boolean bit1 = true;
    boolean bit2 = true;
    int index = 127;
    void setBlack() {
        this.bit1 = false;
        this.bit2 = false;
        this.index = 124;
    }
    void setDark() {
        this.bit1 = false;
        this.bit2 = true;
        this.index = 125;
    }
    void setLight() {
        this.bit1 = true;
        this.bit2 = false;
        this.index = 126;
    }
    void setWhite() {
        this.bit1 = true;
        this.bit2 = true;
        this.index = 127;
    }
    boolean isWhite() {
        return this.bit1 && this.bit2;
    }
    boolean isBlack() {
        return !this.bit1 && !this.bit1;
    }
    int getTileIndex() {
        return index;
    }
    boolean isSameColor(Color color) {
        return this.bit1 == color.bit1 && this.bit2 == color.bit2;
    }

}
class BackgroundMapMod {
    protected Color color = new Color();
    protected boolean user = false;
    protected boolean changed = false;
    int WIDTH = 32;
    int HEIGHT = 32;

    void setColor(Color c) {
        if (!this.color.isSameColor(c)) {
            this.changed = true;
            this.color = c;
        }
    }


    void setUser() {
        this.changed = true;
        this.user = true;
    }
    boolean hasChanged() {
        if (changed) {
            changed = false;
        }
        return changed;
    }

    Color getColor() {
        return this.color;
    }
}
class DrawEvent {
    protected int tileIndex;
    protected int x;
    protected int y;
    protected DrawEvent next = null;

    int getTileIndex() {
        return tileIndex;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    DrawEvent getNext() {
        return next;
    }

    void init(int index, int x, int y) {
        this.tileIndex = index;
        this.x = x;
        this.y = y;
    }
    void setNext(DrawEvent event) {
        this.next = event;
    }
    boolean hasNext() {
        return this.next != null;
    }
}
class DrawEventList {
    protected DrawEvent first = null;
    protected DrawEvent last = null;
    void add(int index, int x, int y) {
        DrawEvent event = new DrawEvent();
        event.init(index, x, y);
        if (this.first == null) {
            this.first = event;
            this.last = event;
        } else {
            this.last.setNext(event);
            this.last = event;
        }
    }
    DrawEvent getFirst() {
        return this.first;
    }
}

class Utils {
    int pow(int x, int exposant) {
        int resultat = 1;
        if (exposant == 0) {
            return 1;
        }
        while (exposant > 0) {
            if (exposant % 2 == 1) {
                resultat = resultat * x;
            }
            x = x * x;
            exposant = exposant / 2;
        }
    }
    void setBackGroundInTileMap(int index) asm (
        "
        ld hl, sp + 4
        ld a, [hl]
        ld hl, $9800
        ld bc, $400
        setBackGroundInTileMapLoop:
            ld [hli], a
            dec bc
            ld a, b
            or a, c
            jp nz, setBackGroundInTileMapLoop ; Jump to COpyTiles, if the z flag is not set. (the last operation had a non zero result)
        ret
        "
    );
    void pushInTileMap(int x, int y, int tileIndex) asm(
            "
            ; a = y
            ld hl, sp + 6
            ld a, [hl]

            ; b = x
            ld hl, sp + 4
            ld b, [hl]

            ; c = value
            ld hl, sp + 8
            ld c, [hl]

            ; si y == 0 on passe à la boucle des colonnes
            or a, a
            jp z, yEqualsZero

            ; hl c est la premiere adresse de la map
            ld hl, $9800
            ; de = 32
            ld e, 32
            ld d, 0

            ; on incremente hl de y * 32
            yLinesLoop:
                add hl, de
                dec a
                jp nz, yLinesLoop

            yEqualsZero:
            ld e, b
            add hl, de ;hl += x;
            ld [hl], c
            ret
            "
            ); // TODO VRAIMENT PAS SÛR



}