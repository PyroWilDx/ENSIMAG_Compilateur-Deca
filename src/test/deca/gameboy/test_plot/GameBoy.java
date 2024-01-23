#include "Utils.java"

class GameBoy {
    DrawEventList drawEvents = new DrawEventList();
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
    protected BackgroundMapMod map = new BackgroundMapMod();
    int WHITE = 124;
    int LIGHT = 126;
    int BLACK = 127;
    int DARK = 125;
    protected boolean firstUpdate = true;

    void init() {
        //Utils u = new Utils();
        //BackgroundMapMod b = new BackgroundMapMod();
        //utils = u;
        //map = b;
        drawEvents.init();
        //this.drawEvents.init();
        //WHITE.setWhite();
        //BLACK.setBlack();
        //DARK.setDark();
        //LIGHT.setLight();
        //this.setBackgroundColor(DARK);
        this.asmInit();
        this.turnScreenOn();

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

    int getBackgroundColor() {
        return this.map.getColor();
    }


    boolean updateScreen() {
        int cc;
        int xxx, yyy, indexxx;
        if (this.isInVBlank() && this.notTooMuchVBlank()) {
            if (this.firstUpdate) {
                this.initDisplayRegisters();
                this.firstUpdate = false;
            }

            //
            //this.turnScreenOff();
            //this.sleep(1);
            //this.waitVBlank();
            if (this.map.hasChanged()) {
                this.turnScreenOff();
                this.map.setStateUpdated();
                cc = map.getColor();
                this.copyColorIntoMap(cc);
                this.turnScreenOn();
            //this.copyColorIntoMap(126);
            }
            //this.waitVDraw();
            //this.waitVBlank();
            //this.utils.pushInTileMap(1, 10, WHITE);
            this.drawEvents.drawList();
            //this.turnScreenOn();
            //this.waitVDraw();
            //his.sleep(2);
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
    boolean notTooMuchVBlank() asm (
            "
    ld h, 0
    ld l, 0
    ld a, [rLY]
    cp 224
    jp nc, notTooMuchVBlank
    ld l, $ff
    ld h, $ff
    notTooMuchVBlank:
    ret
        "
                );
    void turnScreenOff() asm (
        "
        ;call WaitForOneVBlank
        ; Turn the LCD off
        ld a, 0
        ld [rLCDC], a
        ret
        "
    );
    void waitVBlank() asm (
            "
    call WaitForOneVBlank
            ret
        "
    );
    void waitVDraw() asm (
            "
    waitVDrawLoop:

    ld a, [rLY] ; Copy the vertical line to a
    dec a
    or a, a; Check if the vertical line (in a) is 0
    jp z, waitVDrawLoop
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

    void sleep(int i) asm (
            "
            ld hl, sp + 4
            ld c, [hl]
            sleepLoop1:
                ld hl, $a3a; une centi seconde;
                sleepLoop2:
                    dec hl
                    ld a, l
                    or a, h
                    jp nz, sleepLoop2
                dec c
                ld a, c
                or a, a
                jp nz, sleepLoop1
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
        this.setTile(color, x, y);
        //this.setTile(color, x, y);
        //int cc;
        //this.waitVBlank();
        //this.turnScreenOff();
        //if (this.map.hasChanged()) {
        //    this.map.setStateUpdated();
        //    cc = map.getColor();
        //    this.copyColorIntoMap(cc);
        //    //this.copyColorIntoMap(126);
        //}
        //utils.pushInTileMap(x, y, color);
        //this.turnScreenOn();
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
        call WaitForOneVBlank

                ; Turn the LCD off
    ld a, 0
    ld [rLCDC], a


            ; init display reg
    ; During the first (blank) frame, initialize display registers
    ld a, %11100100
    ld [rBGP], a
            ; On met les tiles elementaires dans la mémoire
    ld de, ElementaryTiles
    ld hl, $97c0; Ce seront les quatres dernières tiles
    ld bc, ElementaryTilesEnd - ElementaryTiles
    call CopyDEintoMemoryAtHL

    ;call waitVDrawLoop
    ;call WaitForOneVBlank

    ld de, wLetters
    ld hl, $9620; Ce seront les quatres dernières tiles
    ld bc, wLettersEnd - wLetters
    call CopyDEintoMemoryAtHL

    ld b, 160
    ld hl, _OAMRAM

    ClearOAM:
    ld [hli], a
    dec b
    jp nz, ClearOAM

    call initVariables
            ;ld [hl], $ff

    ; Turn the LCD on
    ld a, LCDCF_ON | LCDCF_BGON | LCDCF_OBJON
    ld [rLCDC], a

            ret


    ;ld hl, $97f0
        ret; comme ça on essaie pas d executer la suite

    SECTION \"Elementary Tile data\", ROM0
    ; Les tiles élémentaire
    ElementaryTiles:
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd:

    wLetters:
    db $00,$00,$38,$38,$04,$04,$3c,$3c,$44,$44,$44,$44,$3c,$3c,$00,$00 ; a
    db $40,$40,$58,$58,$64,$64,$44,$44,$44,$44,$44,$44,$78,$78,$00,$00 ; b
    db $00,$00,$38,$38,$44,$44,$40,$40,$40,$40,$44,$44,$38,$38,$00,$00 ; c
    db $04,$04,$34,$34,$4c,$4c,$44,$44,$44,$44,$44,$44,$3c,$3c,$00,$00 ; d
    db $00,$00,$38,$38,$44,$44,$7c,$7c,$40,$40,$44,$44,$38,$38,$00,$00 ; e
    db $0c,$0c,$10,$10,$3c,$3c,$10,$10,$10,$10,$10,$10,$10,$10,$10,$10 ; f
    db $00,$00,$3c,$3c,$44,$44,$44,$44,$44,$44,$3c,$3c,$04,$04,$38,$38 ; g
    db $40,$40,$58,$58,$64,$64,$44,$44,$44,$44,$44,$44,$44,$44,$00,$00 ; h
    db $00,$00,$30,$30,$10,$10,$10,$10,$10,$10,$10,$10,$10,$10,$00,$00 ; i
    db $00,$00,$18,$18,$08,$08,$08,$08,$08,$08,$08,$08,$08,$08,$30,$30 ; j
    db $20,$20,$24,$24,$28,$28,$30,$30,$28,$28,$24,$24,$22,$22,$00,$00 ; k
    db $30,$30,$10,$10,$10,$10,$10,$10,$10,$10,$10,$10,$10,$10,$00,$00 ; l
    db $00,$00,$fc,$fc,$92,$92,$92,$92,$92,$92,$92,$92,$92,$92,$00,$00 ; m
    db $00,$00,$78,$78,$44,$44,$44,$44,$44,$44,$44,$44,$44,$44,$00,$00 ; n
    db $00,$00,$38,$38,$44,$44,$44,$44,$44,$44,$44,$44,$38,$38,$00,$00 ; o
    db $00,$00,$78,$78,$44,$44,$44,$44,$44,$44,$64,$64,$58,$58,$40,$40 ; p
    db $00,$00,$3c,$3c,$44,$44,$44,$44,$44,$44,$4c,$4c,$34,$34,$04,$04 ; q
    db $00,$00,$2c,$2c,$30,$30,$20,$20,$20,$20,$20,$20,$20,$20,$00,$00 ; r
    db $00,$00,$3c,$3c,$40,$40,$38,$38,$04,$04,$04,$04,$78,$78,$00,$00 ; s
    db $10,$10,$3c,$3c,$10,$10,$10,$10,$10,$10,$10,$10,$0c,$0c,$00,$00 ; t
    db $00,$00,$44,$44,$44,$44,$44,$44,$44,$44,$44,$44,$3c,$3c,$00,$00 ; u
    db $00,$00,$44,$44,$44,$44,$28,$28,$28,$28,$10,$10,$10,$10,$00,$00 ; v
    db $00,$00,$91,$91,$91,$91,$4a,$4a,$5a,$5a,$24,$24,$24,$24,$00,$00 ; w
    db $00,$00,$44,$44,$28,$28,$10,$10,$10,$10,$28,$28,$44,$44,$00,$00 ; x
    db $00,$00,$44,$44,$44,$44,$28,$28,$28,$28,$10,$10,$10,$10,$60,$60 ; y
    db $00,$00,$3c,$3c,$04,$04,$08,$08,$10,$10,$20,$20,$3c,$3c,$00,$00 ; z
    wLettersEnd:

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
        ld l, 0
        jp nz ,notPressed
        ld hl, $ff
        notPressed:
        ret
        "
    );
}
