#include "Utils.decah"

class Tile {
    int index;
    void placeForeGround{

    }
    void place(boolean foreground, int x, int y) {
        int mapOffset = (y * 32) + x;

        // TODO
    }
    void setTilePixel(int x, int y, Color color) {
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
    boolean isWhite() {
        return this.bit1 && this.bit2;
    }
    boolean isBlack() {
        return !this.bit1 && !this.bit1;
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
    "
    ; Les tiles élémentaire
    ElementaryTiles :
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd :

    ; On met ces tiles elementaires dans la mémoire
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

    ; Label menant à une map toute blanche
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

    ; Label menant à une map toute blanche
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

    ; Label pour avec la taille de la map dans bc
    ; et l adresse de la map dans de
    CopyTilemap:
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    jp nz, CopyTilemap
    " // TODO commencer la gameloop
    ); // TODO pour faire les trucs assembleurs de bases genre main loop et tout jsp

    void setTileMapWhite(Color color) asm(
        "
        ld de, WhiteTilemap
        ld hl, $9800
        ld bc, WhiteTilemapEnd - WhiteTilemap
        jp CopyTileMap
        "
        );
    void setTileMapBlack(Color color) asm(
    "
    ld de, BlackTilemap
    ld hl, $9800
    ld bc, BlackTilemapEnd - BlackTilemap
    jp CopyTileMap
    "
    );
}
