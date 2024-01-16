#include "Utils.decah"

class Tile {
    int index;
    void placeForeGround{

    }
    void place(boolean foreground, int x, int y) {

    }
    void setBlack() {
        this.index = 0;
    }
    void setDark() {
        this.index = 1;
    }
    void setLight() {
        this.index = 2
    }
    void setWhite() {
        this.index = 3;
    }

}
class Color {
    boolean bit1 = 0;
    boolean bit2 = 0;
    void setBlack() {
        this.bit1 = false;
        this.bit2 = false;
    }
    void setDark() {
        this.bit1 = false;
        this.bit2 = true;
    }
    void setLight() {
        this.bit1 = true;
        this.bit2 = false;
    }
    void setWhite() {
        this.bit1 = true;
        this.bit2 = true;
    }


}

class GameBoy {
    protected int width = 160;
    protected int height = 144;
    protected int firstVRamAdress = 32768;
    protected utils = new Utils();
    protected isWhiteMapLabelDefined = false;

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }
    void init () asm (
    "ElementaryTiles :
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd :
    ld de, ElementaryTiles
    ld hl, $9000
    ld bc, ElementaryTiles - ElementaryTilesEnd
    CopyElementaryTiles :
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    jp nz, CopyElementaryTiles
    WhiteTilemap:
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    db $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, $01, 0,0,0,0,0,0,0,0,0,0,0,0
    WhiteTilemap:
    BlackTilemap:
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    db $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, $00, 0,0,0,0,0,0,0,0,0,0,0,0
    BlackTilemap:
    CopyTilemap:
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    " // TODO commencer la gameloop
    ); // TODO pour faire les trucs assembleurs de bases genre main loop et tout jsp
    void setTileMap(Color color) {

    }
    void setTileMap(Color color) asm(
        "ld de, WhiteTilemap
        ld hl, $9800
        ld bc, WhiteTilemapEnd - WhiteTilemap
        ld de, WhiteTilemap
        ld hl, $9800
        ld bc, WhiteTilemapEnd - WhiteTilemap
        jp CopyTileMap"
            )
    void setTileMap(Color color) asm(
    "ld de, WhiteTilemap
    ld hl, $9800
    ld bc, WhiteTilemapEnd - WhiteTilemap
    ld de, WhiteTilemap
    ld hl, $9800
    ld bc, WhiteTilemapEnd - WhiteTilemap
    jp CopyTileMap"
            )

    void setPixel(int x, int y) asm (
        "load a,b
        cp 24*8
        ret nc
        push bc
            push de"
            
    ); // TODO;
    int createTile() asm();
    void setTilePixel(int numTile,int x, int y, Color color) {
        int tileAddr = this.firstVRamAdress + numTile * 16; // une tile c'est 16 octets
        int addr1 = tileAddr + 2*y;
        int addr2 = addr1 + 1;
        int oct1 = Utils.get(addr1);
        int oct2 = Utils.get(addr2);
        int newValue1 = oct1;
        int newValue2 = oct2;
        int powerOfTwo = this.utils.pow(2, 7 - x);
        boolean bit1 = ((oct1 / powerOfTwo) % 2) == 1;
        boolean bit2 = ((oct2 / powerOfTwo) % 2) == 1;
        if (bit1 != color.bit1) {
            if (color.bit1) {
                newValue1 = newValue1 + powerOfTwo;
            }
            else {
                newValue1 = newValue1 - powerOfTwo;
            }
        }
        if (bit2 != color.bit2) {
            if (color.bit2) {
                newValue2 = newValue2 + powerOfTwo;
            }
            else {
                newValue2 = newValue2 - powerOfTwo;
            }
        }
        utils.push(addr1, newValue1);
        utils.push(addr2, newValue2);
    };
    void setTileColor(int numTile, Color color) {
        int tileAddr = this.firstVRamAdress + numTile * 16; // une tile c'est 16 octets
        int i = 0;
        while (i < 16) {

        }
    }
    void setMemory
    void drawRect(int x, int y, int with, int height) {
        int i = x;
        int j = y;
        int xEnd = x + width;
        int yEnd = y + height;
        while (j <= yEnd) {
            while (i <= xEnd) {
                setPixel();
            }
        }
    }

    public ld getA() {
        return a;
    }

    public void setA(ld a) {
        this.a = a;
    }

    public int getFirstVRamAdress() {
        return firstVRamAdress;
    }

    public ld getElementaryTiles() {
        return ElementaryTiles;
    }
}