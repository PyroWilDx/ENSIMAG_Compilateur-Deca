#include "Utils.java"

class GameBoy {
    protected DrawEventList drawEvents;
    int WIDTH = 20;
    protected int pixelWidth = 160;
    int HEIGHT = 18;
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
    protected BackgroundMapMod map;
    int WHITE = 124;
    int LIGHT = 126;
    int BLACK = 127;
    int DARK = 125;
    protected boolean firstUpdate = true;

    void init() {
        //Utils u = new Utils();
        BackgroundMapMod b = new BackgroundMapMod();
        DrawEventList d = new DrawEventList();
        //utils = u;
        map = b;
        d.init();
        drawEvents = d;
        //this.drawEvents.init();
        //WHITE.setWhite();
        //BLACK.setBlack();
        //DARK.setDark();
        //LIGHT.setLight();
        //this.setBackgroundColor(DARK);
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
        int cc;
        int xxx, yyy, indexxx;
        if (this.isInVBlank()) {
            if (this.firstUpdate) {
                this.initDisplayRegisters();
                this.firstUpdate = false;
            }
            this.turnScreenOff();
            if (this.map.hasChanged()) {
                this.map.setStateUpdated();
                cc = map.getColor();
                this.copyColorIntoMap(cc);
                //this.copyColorIntoMap(126);
            }
            this.utils.pushInTileMap(10, 10, BLACK);
            //this.drawEvents.drawList();
            this.turnScreenOn();
            return true;
        }
        return false;
    }
    boolean isInVBlank() asm (
        "
        ld h, 0
        ld l, 0
        ld a, [rLY]
        cp 144
        jp c, notVBlank
        ld l, $ff
        ld h, $ff
        notVBlank:
        ret
        "
    );
    void turnScreenOff() asm (
        "
        call WaitForOneVBlank
        ; Turn the LCD off
        ld a, 0
        ld [rLCDC], a
        ret
        "
    );
    void turnScreenOn() asm (
        "
    ; Turn the LCD on
    ld a, LCDCF_ON | LCDCF_BGON | LCDCF_OBJON
    ld [rLCDC], a
    ret
        "
    );
    void initDisplayRegisters() asm (
        "
        ; During the first (blank) frame, initialize display registers
        ld a, %11100100
        ld [rBGP], a
        ret
        "
    );
    void setTile(int tileIndex, int x, int y) {
        //DrawEvent e = new DrawEvent();
        //e.init(tileIndex, x, y);
        this.drawEvents.add(tileIndex, x, y);
    }
    void setColor(int color, int x, int y) {
        //
        //this.setTile(color, x, y);
        utils.pushInTileMap(10,15, 127);
    }
    void rien() {}


    //
    void setBackgroundColor(int color) {
        this.map.setColor(color);
    }
    void copyColorIntoMap(int c) {
        //int index = c.getTileIndex();
        int index = c;
        //index = 126;
        //println();
        //this.testtt(index);
        //this.stop();
        this.utils.setBackGroundInTileMap(index);
    }

    void stop() asm (
            "
    stoppp:
            halt
            jp stoppp
        "
                );
    void asmInit () asm (
        "
        call initVariables
        call WaitForOneVBlank
            ; On met les tiles elementaires dans la mémoire
        ld de, ElementaryTiles
        ld hl, $97c0; Ce seront les quatres dernières tiles
        ld bc, ElementaryTilesEnd - ElementaryTiles
        call CopyDEintoMemoryAtHL



    ld hl, $97f0
    ld [hl], $ff
        ret; comme ça on essaie pas d executer la suite

    SECTION \"Elementary Tile data\", ROM0
    ; Les tiles élémentaire
    ElementaryTiles:
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd:

    SECTION \"Utils\", ROM0
    initVariables:
    ld a, 0
    ld [wFrameCounter], a
    ld [wCurKeys], a
    ld [wNewKeys], a
            ret
    CopyDEintoMemoryAtHL:
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    jp nz, CopyDEintoMemoryAtHL ; Jump to COpyTiles, if the z flag is not set. (the last operation had a non zero result)
    ret;
    SECTION \"Variables\", WRAM0

    wVBlankCount: db
    wFrameCounter: db
    wCurKeys: db
    wNewKeys: db


    SECTION \"VBlankFunctions\", ROM0

    WaitForOneVBlank::

    ; Wait a small amount of time
            ; Save our count in this variable
    ld a, 1
    ld [wVBlankCount], a

    WaitForVBlankFunction::

    WaitForVBlankFunction_Loop::

    ld a, [rLY] ; Copy the vertical line to a
    cp 144 ; Check if the vertical line (in a) is 0
    jp c, WaitForVBlankFunction_Loop ; A conditional jump. The condition is that 'c' is set, the last operation overflowed

    ld a, [wVBlankCount]
    sub a, 1
    ld [wVBlankCount], a
    ret z

    WaitForVBlankFunction_Loop2::

    ld a, [rLY] ; Copy the vertical line to a
    cp 144 ; Check if the vertical line (in a) is 0
    jp nc, WaitForVBlankFunction_Loop2 ; A conditional jump. The condition is that 'c' is set, the last operation overflowed

    jp WaitForVBlankFunction_Loop

            ; ANCHOR_END: vblank-utils
        "
    ); //
    boolean keyPressed(int pad) asm(
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
