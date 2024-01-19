#include "Utils.java"



//class Tile {
//    protected int index;
//    protected int firstTileAdress = 36864;
//    protected Utils utils = new Utils();
//    void placeForeGround(){
//
//    }
//    void place(boolean foreground, int x, int y) {
//        int mapOffset = (y * 32) + x;
//
//        // TODO
//    }
//    void setTilePixel(int x, int y, Color color) {
//        int tileAddr = this.firstTileAdress + this.index * 16; // une tile c'est 16 octets
//        int addr1 = tileAddr + 2*y;
//        int addr2 = addr1 + 1;
//        int oct1 = utils.get(addr1);
//        int oct2 = utils.get(addr2);
//        int newValue1 = oct1;
//        int newValue2 = oct2;
//        int powerOfTwo = this.utils.pow(2, 7 - x);
//        boolean bit1 = ((oct1 / powerOfTwo) % 2) == 1;
//        boolean bit2 = ((oct2 / powerOfTwo) % 2) == 1;
//        if (bit1 != color.bit1) {
//            if (color.bit1) {
//                newValue1 = newValue1 + powerOfTwo;
//            }
//            else {
//                newValue1 = newValue1 - powerOfTwo;
//            }
//        }
//        if (bit2 != color.bit2) {
//            if (color.bit2) {
//                newValue2 = newValue2 + powerOfTwo;
//            }
//            else {
//                newValue2 = newValue2 - powerOfTwo;
//            }
//        }
//        utils.push(addr1, newValue1);
//        utils.push(addr2, newValue2);
//    }
//    void setBlack() {
//        this.index = 0;
//    }
//    void setDark() {
//        this.index = 1;
//    }
//    void setLight() {
//        this.index = 2;
//    }
//    void setWhite() {
//        this.index = 3;
//    }
//
//}
class Color {
    boolean bit1 = false;
    boolean bit2 = false;
    int index = 124;
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

}

class GameBoy {
    protected DrawEventList drawEvents = new DrawEventList();
    protected int width = 20;
    protected int pixelWidth = 160;
    protected int height = 18;
    protected int pixelHeight = 144;
    protected Utils utils = new Utils();
    protected BackGroundMapMod map = new backGroundMapMod();
    protected Color WHITE = new Color();
    protected Color LIGHT = new Color();
    protected Color DARK = new Color();
    protected Color BLACK = new Color();
    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    void init() {
        WHITE.setWhite();
        BLACK.setBlack();
        DARK.setDark();
        LIGHT.setLight();
        //this.includeHardware();
        this.includeTextMacro();
        this.includeTextUtils();
        this.includeMemoryUtils();
        this.includeInputUtils();
        this.includeBackGroundUtils();
        this.includeMath_asm();
        this.includeVBlankUtils();
    }
    void Owninit () asm (
    "
    ; Shut down audio circuitry
    ld a, 0
    ld [rNR52], a

    ; On met les tiles elementaires dans la mémoire
    ld de, ElementaryTiles
    ld hl, $97c0; Ce seront les quatres dernières tiles
    ld bc, ElementaryTiles - ElementaryTilesEnd
    call CopyDEintoMemoryAtHL

    ; On met la map toute blanche
    ld de, WhiteTilemap
    ld hl, $9800
    ld bc, WhiteTilemap - WhiteTilemapEnd
    call CopyDEintoMemoryAtHL


    ; Les tiles élémentaire
    ElementaryTiles :
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd :

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
    WhiteTilemapEnd:

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
    BlackTilemapEnd:
    " // TODO commencer la gameloop
    ); // TODO pour faire les trucs assembleurs de bases genre main loop et tout jsp

    void updateScreen() {
        DrawEvent event = this.drawEvents.getFirst();
        this.turnScreenOff();
        if (this.map.hasChanged()) {
            if (this.map.isWhite) {
                this.copyWhiteMapIntoMemory();
            }
        }
        while(event.hasNext()) {
            this.utils.push(event.getX(), event.getY(), event.getTileIndex());
            event = event.getNext();
        }
        this.turnScreenOn();
    }
    void turnScreenOff() asm (
        "
        call WaitForOneVBlank
        ; Turn the LCD off
        ld a, 0
        ld [rLCDC], a
        "
    );
    void turnScreenOn() asm (
        "
        ; Turn the LCD on
        ld a, LCDCF_ON | LCDCF_BGON
        ld [rLCDC], a
        "
    );
    void setTile(int tileIndex, int x, int y) {
        this.drawEvents.add(tileIndex, x, y);
    }
    void setbackGroundWhite() {
        if (!this.map.isWhite) {
            this.map.setWhite();
        }
    }
    void copyWhiteMapIntoMemory() asm (
        "
        ld de, WhiteTilemap
        ld hl, $9800
        ld bc, WhiteTilemap - WhiteTilemapEnd
        call CopyDEintoMemoryAtHL
        "
    );
    void copyBlackMapIntoMemory() asm (
            "
    ld de, BlackTilemap
    ld hl, $9800
    ld bc, WhiteTilemap - WhiteTilemapEnd
    call CopyDEintoMemoryAtHL
        "
                );
    void
    void setTileMapBlack(Color color) asm(
    "
    ld de, BlackTilemap
    ld hl, $9800
    ld bc, BlackTilemapEnd - BlackTilemap
    call CopyDEintoMemoryAtHL
    "
    );

    void includeVBlankUtils() asm (
        #include "vblank_utils_asm"
    );
    void includeMemoryUtils() asm (
        #include "memory_utils_asm"
    );
    void includeInputUtils() asm (
        #include "input_utils_asm"
    );

    void includeBackGroundUtils() asm (
        #include "background_utils_asm"
    );

    void includeMath_asm() asm(
        #include "math_asm"
    );

    /////////////////////////////////////////////////////////////////////
    // TEXT
    /////////////////////////////////////////////////////////////////////
    void includeTextUtils() asm (
        #include "text_utils_asm"
    );
    void includeTextMacro() asm (
        #include "text_macro_inc"
    );

    /////////////////////////////////////////////////////////////////////////////////////
    // HARDWARE
    //////////////////////////////////////////////////////////////////////////////////////
    //void includeHardware() asm (
      //  #include "hardware_inc"
    //);
    void endGame() asm (
        "
        Done:
        halt
        jp Done
        "
    );
}
