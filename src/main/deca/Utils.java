
class BackGroundMapMod {
    protected boolean white = true;
    protected boolean black = false;
    protected boolean user = false;
    protected boolean changed = false;

    public void setWhite() {
        this.changed = true;
        this.white = true;
        this.black = false;
        this.user = false;
    }

    public void setBlack() {
        this.changed = true;
        this.white = false;
        this.black = true;
        this.user = false;
    }

    public void setUser() {
        this.changed = true;
        this.white = false;
        this.black = false;
        this.user = true;
    }

    public void setChanged(boolean changed) {
        this.changed = changed;
    }

    boolean isWhite() {
        return white;
    }

    boolean isBlack() {
        return black;
    }

    boolean isUser() {
        return user;
    }
    boolean hasChanged() {
        return changed;
    }
}
class DrawEvent {
    protected int tileIndex;
    protected int x;
    protected int y;
    protected int next = null;

    int getTileIndex() {
        return tileIndex;
    }

    int getX() {
        return x;
    }

    int getY() {
        return y;
    }

    int getNext() {
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
class DrawEventList() {
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
    int get(int addr) asm(
            "
            ld bc, 49664
            ld [bc], bc
            "
            ); // TODO VRAIMENT PAS SUR.
    void pushInTileMap(int x, int y, int tileIndex) asm(
            "
            ; a = y
            ld hl, sp + 7
            ld a, [hl]

            ; b = x
            ld hl, sp + 6
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
            yLinesLoop :
                add hl, de
                dec a
                jp nz, yLinesLoop

            yEqualsZero :
            ld e, b
            add hl, de ;hl += x;
            ld [hl], c
            "
            ); // TODO VRAIMENT PAS SÛR
}