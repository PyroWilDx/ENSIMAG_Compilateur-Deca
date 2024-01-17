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
    protected DoubleLinkedListEvents drawEvents = new DoubleLinkedListEvents();
    protected int width = 160;
    protected int height = 144;
    protected Utils utils = new Utils();
    protected boolean isWhiteMapLabelDefined = false;

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }

    void init() {
        this.includeHardware();
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

    ; Les tiles élémentaire
    ElementaryTiles :
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd :

    ElementaryTiles :
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd :

    WhiteTile :
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    WhiteTileEnd :

    BlackTile :
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    BlackTileEnd :




    ; On met les tiles elementaires dans la mémoire
    jp
    ld de, WhiteTile
    ld hl, $9000
    ld bc, WhiteTile - WhiteTile
    call Memcopy

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


    UpdateKeys:
    ; Poll half the controller
    ld a, P1F_GET_BTN
    call .onenibble
    ld b, a ; B7-4 = 1; B3-0 = unpressed buttons

            ; Poll the other half
    ld a, P1F_GET_DPAD
    call .onenibble
    swap a ; A3-0 = unpressed directions; A7-4 = 1
    xor a, b ; A = pressed buttons + directions
    ld b, a ; B = pressed buttons + directions

    ; And release the controller
    ld a, P1F_GET_NONE
    ldh [rP1], a

    ; Combine with previous wCurKeys to make wNewKeys
    ld a, [wCurKeys]
    xor a, b ; A = keys that changed state
    and a, b ; A = keys that changed to pressed
    ld [wNewKeys], a
    ld a, b
    ld [wCurKeys], a
            ret

    " // TODO commencer la gameloop
    ); // TODO pour faire les trucs assembleurs de bases genre main loop et tout jsp

    void clearBackGround() asm (
        "
        call WaitForOneVBlank
        call ClearBackground
        "
    );
    void set(int indice, int x, int y) {
        this.clearBackGround();
        int mapOffset = 0x9800 + (y * 32) + x;
        this.utils.push(mapOffset, indice);
    }
    void setTileMapWhite(Color color) asm(
        "
        ld de, WhiteTilemap
        ld hl, $9800
        ld bc, WhiteTilemapEnd - WhiteTilemap
        call Memcopy
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
    void includeHardware() asm (
        #include "hardware_inc"
    );
    void endGame() asm (
        "
        Done:
        halt
        jp Done
        "
    );
}
