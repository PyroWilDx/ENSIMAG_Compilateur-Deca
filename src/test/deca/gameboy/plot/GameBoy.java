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


class GameBoy {
    protected DrawEventList drawEvents = new DrawEventList();
    protected int width = 20;
    protected int pixelWidth = 160;
    protected int height = 18;
    protected int pixelHeight = 144;


    int UP_KEY = 64;
    int DOWN_KEY = 128;
    int LEFT_KEY = 32;
    int RIGHT_KEY = 16;
    int A_KEY = 1;
    int B_KEY = 2;
    int SELECT_KEY = 4;
    int START_KEY = 8;
    protected Utils utils = new Utils();
    protected BackgroundMapMod map = new BackgroundMapMod();
    Color WHITE = new Color();
    Color LIGHT = new Color();
    Color DARK = new Color();
    Color BLACK = new Color();
    protected boolean firstUpdate = true;
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
        this.setBackgroundColor(WHITE);
        this.asmInit();

        // TODO faudra en fait mettre tous ces trucs au début du fichier avec le compilateur
        //this.includeHardware();
        //this.includeTextMacro();
        //this.includeTextUtils();
        //this.includeMemoryUtils();
        //this.includeInputUtils();
        //this.includeBackGroundUtils();
        //this.includeMath_asm();
        //this.includeVBlankUtils();
    }


    boolean updateScreen() {
        DrawEvent event = this.drawEvents.getFirst();
        if (this.isInVBlank()) {
            if (this.firstUpdate) {
                this.initDisplayRegisters();
                this.firstUpdate = false;
            }
            this.turnScreenOff();
            if (this.map.hasChanged()) {
                this.copyColorIntoMap(this.map.getColor());
            }
            while (event.hasNext()) {
                this.utils.pushInTileMap(event.getX(), event.getY(), event.getTileIndex());
                event = event.getNext();
            }
            this.turnScreenOn();
            return true;
        }
        return false;
    }
    boolean isInVBlank() asm (
        "
        ld h, 0

        ret
        "
    );
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
        ld a, LCDCF_ON | LCDCF_BGON | LCDCF_OBJON
        ld [rLCDC], a
        "
    );
    void initDisplayRegisters() asm (
        "
        ; During the first (blank) frame, initialize display registers
        ld a, %11100100
        ld [rBGP], a
        "
    );
    void setTile(int tileIndex, int x, int y) {
        this.drawEvents.add(tileIndex, x, y);
    }
    void setColor(Color color, int x, int y) {
        this.setTile(color.getTileIndex(), x, y);
    }

    //
    void setBackgroundColor(Color color) {
        this.map.setColor(color);
    }
    void copyColorIntoMap(Color color) {
        int index = color.getTileIndex();
        this.utils.setBackGroundInTileMap(index);
    }
    void asmInit () asm (
        "
        ; On met les tiles elementaires dans la mémoire
        ld de, ElementaryTiles
        ld hl, $97c0; Ce seront les quatres dernières tiles
        ld bc, ElementaryTiles - ElementaryTilesEnd
        call CopyDEintoMemoryAtHL

        ret; comme ça on essaie pas d executer la suite

        ; Les tiles élémentaire
        ElementaryTiles:
        db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
        db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
        db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
        db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
        ElementaryTilesEnd:
        "
    ); //
    boolean KeyPressed(int pad) asm(
        "
        ld a, [wNewKeys]
        ld b, a
        ld hl, sp + 4
        ld a, [hl]
        and a, b
        ld h, 0
        jp nz ,notPressed
        ld l, 1
        ret
        notPressed:
        ld l, 0
        ret
        "
    );
}
