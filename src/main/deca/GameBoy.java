#include "Utils.java"

class Tile {
    protected int index;
    protected int firstTileAdress = 36864;
    protected Utils utils = new Utils();
    void placeForeGround(){

    }
    void place(boolean foreground, int x, int y) {
        int mapOffset = (y * 32) + x;

        // TODO
    }
    void setTilePixel(int x, int y, Color color) {
        int tileAddr = this.firstTileAdress + this.index * 16; // une tile c'est 16 octets
        int addr1 = tileAddr + 2*y;
        int addr2 = addr1 + 1;
        int oct1 = utils.get(addr1);
        int oct2 = utils.get(addr2);
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
    }
    void setBlack() {
        this.index = 0;
    }
    void setDark() {
        this.index = 1;
    }
    void setLight() {
        this.index = 2;
    }
    void setWhite() {
        this.index = 3;
    }

}
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

    void init () asm (
    "
    WaitVBlank:
    ; Wait for screen drawing to finish
    ld a, [rLY]
    cp 144
    jp c, WaitVBlank
    ret

    ; Les tiles élémentaire
    ElementaryTiles :
    db $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00, $00,$00
    db $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff, $00,$ff
    db $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00, $ff,$00
    db $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff, $ff,$ff
    ElementaryTilesEnd :

    ; Hourglass font from https://damieng.com/zx-origins
    CharactersTiles :
    defb &00,&00,&00,&00,&00,&00,&00,&00 ;
    defb &10,&10,&10,&10,&10,&00,&10,&00 ; !
    defb &28,&28,&28,&00,&00,&00,&00,&00 ; "
    defb &28,&28,&7c,&28,&7c,&28,&28,&00 ; #
    defb &10,&3c,&50,&38,&14,&78,&10,&00 ; $
    defb &64,&68,&08,&10,&20,&2c,&4c,&00 ; %
    defb &38,&40,&40,&3c,&48,&48,&3c,&00 ; &
    defb &10,&10,&10,&00,&00,&00,&00,&00 ; '
    defb &08,&10,&20,&20,&20,&10,&08,&00 ; (
    defb &20,&10,&08,&08,&08,&10,&20,&00 ; )
    defb &00,&10,&54,&38,&54,&10,&00,&00 ; *
    defb &00,&10,&10,&7c,&10,&10,&00,&00 ; +
    defb &00,&00,&00,&00,&00,&20,&20,&40 ; ,
    defb &00,&00,&00,&7c,&00,&00,&00,&00 ; -
    defb &00,&00,&00,&00,&00,&40,&40,&00 ; .
    defb &04,&04,&08,&10,&20,&40,&40,&00 ; /
    defb &38,&44,&4c,&54,&64,&44,&38,&00 ; 0
    defb &10,&30,&10,&10,&10,&10,&38,&00 ; 1
    defb &38,&44,&04,&08,&10,&20,&7c,&00 ; 2
    defb &7c,&04,&08,&18,&04,&04,&78,&00 ; 3
    defb &0c,&14,&24,&44,&7c,&04,&04,&00 ; 4
    defb &7c,&40,&40,&78,&04,&04,&78,&00 ; 5
    defb &18,&20,&40,&78,&44,&44,&38,&00 ; 6
    defb &7c,&04,&04,&08,&08,&10,&10,&00 ; 7
    defb &38,&44,&44,&38,&44,&44,&38,&00 ; 8
    defb &38,&44,&44,&3c,&04,&08,&30,&00 ; 9
    defb &00,&00,&10,&10,&00,&10,&10,&00 ; :
    defb &00,&00,&10,&10,&00,&10,&10,&20 ; ;
    defb &04,&08,&10,&20,&10,&08,&04,&00 ; <
    defb &00,&00,&7c,&00,&7c,&00,&00,&00 ; =
    defb &20,&10,&08,&04,&08,&10,&20,&00 ; >
    defb &38,&04,&04,&08,&10,&00,&10,&00 ; ?
    defb &1c,&22,&4e,&52,&52,&4e,&20,&1e ; @
    defb &10,&10,&28,&28,&7c,&44,&44,&00 ; A
    defb &78,&44,&44,&78,&44,&44,&78,&00 ; B
    defb &38,&44,&40,&40,&40,&44,&38,&00 ; C
    defb &78,&44,&44,&44,&44,&44,&78,&00 ; D
    defb &7c,&40,&40,&78,&40,&40,&7c,&00 ; E
    defb &7c,&40,&40,&78,&40,&40,&40,&00 ; F
    defb &3c,&40,&40,&4c,&44,&44,&3c,&00 ; G
    defb &44,&44,&44,&7c,&44,&44,&44,&00 ; H
    defb &38,&10,&10,&10,&10,&10,&38,&00 ; I
    defb &04,&04,&04,&04,&44,&44,&38,&00 ; J
    defb &44,&48,&50,&60,&50,&48,&44,&00 ; K
    defb &40,&40,&40,&40,&40,&40,&7c,&00 ; L
    defb &82,&c6,&aa,&92,&92,&82,&82,&00 ; M
    defb &44,&64,&54,&4c,&44,&44,&44,&00 ; N
    defb &38,&44,&44,&44,&44,&44,&38,&00 ; O
    defb &78,&44,&44,&78,&40,&40,&40,&00 ; P
    defb &38,&44,&44,&44,&54,&48,&34,&04 ; Q
    defb &78,&44,&44,&78,&50,&48,&44,&00 ; R
    defb &3c,&40,&40,&38,&04,&04,&78,&00 ; S
    defb &7c,&10,&10,&10,&10,&10,&10,&00 ; T
    defb &44,&44,&44,&44,&44,&44,&38,&00 ; U
    defb &44,&44,&44,&28,&28,&10,&10,&00 ; V
    defb &92,&92,&aa,&aa,&44,&44,&44,&00 ; W
    defb &44,&44,&28,&10,&28,&44,&44,&00 ; X
    defb &44,&44,&28,&28,&10,&10,&10,&00 ; Y
    defb &7c,&04,&08,&10,&20,&40,&7c,&00 ; Z
    defb &3c,&20,&20,&20,&20,&20,&3c,&00 ; [
    defb &40,&40,&20,&10,&08,&04,&04,&00 ; \
    defb &3c,&04,&04,&04,&04,&04,&3c,&00 ; ]
    defb &10,&38,&54,&10,&10,&10,&00,&00 ; ^
    defb &00,&00,&00,&00,&00,&00,&00,&ff ; _
    defb &1c,&20,&20,&78,&20,&20,&7c,&00 ; £
    defb &00,&38,&04,&3c,&44,&44,&3c,&00 ; a
    defb &40,&58,&64,&44,&44,&44,&78,&00 ; b
    defb &00,&38,&44,&40,&40,&44,&38,&00 ; c
    defb &04,&34,&4c,&44,&44,&44,&3c,&00 ; d
    defb &00,&38,&44,&7c,&40,&44,&38,&00 ; e
    defb &0c,&10,&3c,&10,&10,&10,&10,&10 ; f
    defb &00,&3c,&44,&44,&44,&3c,&04,&38 ; g
    defb &40,&58,&64,&44,&44,&44,&44,&00 ; h
    defb &00,&30,&10,&10,&10,&10,&10,&00 ; i
    defb &00,&18,&08,&08,&08,&08,&08,&30 ; j
    defb &20,&24,&28,&30,&28,&24,&22,&00 ; k
    defb &30,&10,&10,&10,&10,&10,&10,&00 ; l
    defb &00,&fc,&92,&92,&92,&92,&92,&00 ; m
    defb &00,&78,&44,&44,&44,&44,&44,&00 ; n
    defb &00,&38,&44,&44,&44,&44,&38,&00 ; o
    defb &00,&78,&44,&44,&44,&64,&58,&40 ; p
    defb &00,&3c,&44,&44,&44,&4c,&34,&04 ; q
    defb &00,&2c,&30,&20,&20,&20,&20,&00 ; r
    defb &00,&3c,&40,&38,&04,&04,&78,&00 ; s
    defb &10,&3c,&10,&10,&10,&10,&0c,&00 ; t
    defb &00,&44,&44,&44,&44,&44,&3c,&00 ; u
    defb &00,&44,&44,&28,&28,&10,&10,&00 ; v
    defb &00,&91,&91,&4a,&5a,&24,&24,&00 ; w
    defb &00,&44,&28,&10,&10,&28,&44,&00 ; x
    defb &00,&44,&44,&28,&28,&10,&10,&60 ; y
    defb &00,&3c,&04,&08,&10,&20,&3c,&00 ; z
    defb &1c,&10,&10,&60,&10,&10,&1c,&00 ; {
    defb &10,&10,&10,&10,&10,&10,&10,&00 ; |
    defb &70,&10,&10,&0c,&10,&10,&70,&00 ; }
    defb &34,&58,&00,&00,&00,&00,&00,&00 ; ~
    defb &3c,&42,&99,&a5,&a1,&9d,&42,&3c ; ©
    EndCharacterTiles :


    ; On met les tiles elementaires dans la mémoire
    jp
    ld de, ElementaryTiles
    ld hl, $9000
    ld bc, ElementaryTiles - ElementaryTilesEnd
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

    ; Copy bytes from one area to another.
    ; @param de: Source
    ; @param hl: Destination
    ; @param bc: Length
    Memcopy:
    ld a, [de]
    ld [hli], a
    inc de
    dec bc
    ld a, b
    or a, c
    jp nz, Memcopy
    ret

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

    void setTileMapWhite(Color color) asm(
        "
        ld de, WhiteTilemap
        ld hl, $9800
        ld bc, WhiteTilemapEnd - WhiteTilemap
        call Memcopy
        "
        );
    void setTileMapBlack(Color color) asm(
    "
    ld de, BlackTilemap
    ld hl, $9800
    ld bc, BlackTilemapEnd - BlackTilemap
    call Memcopy
    "
    );

    
    void textMacro() asm (
            "
    ; ANCHOR: charmap
    ; The character map for the text-font
    CHARMAP " ", 0
    CHARMAP ".", 24
    CHARMAP "-", 25
    CHARMAP "a", 26
    CHARMAP "b", 27
    CHARMAP "c", 28
    CHARMAP "d", 29
    CHARMAP "e", 30
    CHARMAP "f", 31
    CHARMAP "g", 32
    CHARMAP "h", 33
    CHARMAP "i", 34
    CHARMAP "j", 35
    CHARMAP "k", 36
    CHARMAP "l", 37
    CHARMAP "m", 38
    CHARMAP "n", 39
    CHARMAP "o", 40
    CHARMAP "p", 41
    CHARMAP "q", 42
    CHARMAP "r", 43
    CHARMAP "s", 44
    CHARMAP "t", 45
    CHARMAP "u", 46
    CHARMAP "v", 47
    CHARMAP "w", 48
    CHARMAP "x", 49
    CHARMAP "y", 50
    CHARMAP "z", 51
    ; ANCHOR_END: charmap
        );

    /////////////////////////////////////////////////////////////////////////////////////
    // HARDWARE
    //////////////////////////////////////////////////////////////////////////////////////
    void hardware() asm (
            "
    ;*
    ;* Gameboy Hardware definitions
    ;*
    ;* Based on Jones' hardware.inc
    ;* And based on Carsten Sorensen's ideas.
    ;*
    ;* Rev 1.1 - 15-Jul-97 : Added define check
    ;* Rev 1.2 - 18-Jul-97 : Added revision check macro
            ;* Rev 1.3 - 19-Jul-97 : Modified for RGBASM V1.05
    ;* Rev 1.4 - 27-Jul-97 : Modified for new subroutine prefixes
            ;* Rev 1.5 - 15-Aug-97 : Added _HRAM, PAD, CART defines
    ;*                     :  and Nintendo Logo
    ;* Rev 1.6 - 30-Nov-97 : Added rDIV, rTIMA, rTMA, & rTAC
    ;* Rev 1.7 - 31-Jan-98 : Added _SCRN0, _SCRN1
            ;* Rev 1.8 - 15-Feb-98 : Added rSB, rSC
            ;* Rev 1.9 - 16-Feb-98 : Converted I/O registers to $FFXX format
    ;* Rev 2.0 -           : Added GBC registers
    ;* Rev 2.1 -           : Added MBC5 & cart RAM enable/disable defines
            ;* Rev 2.2 -           : Fixed NR42,NR43, & NR44 equates
            ;* Rev 2.3 -           : Fixed incorrect _HRAM equate
            ;* Rev 2.4 - 27-Apr-13 : Added some cart defines (AntonioND)
            ;* Rev 2.5 - 03-May-15 : Fixed format (AntonioND)
            ;* Rev 2.6 - 09-Apr-16 : Added GBC OAM and cart defines (AntonioND)
            ;* Rev 2.7 - 19-Jan-19 : Added rPCMXX (ISSOtm)
            ;* Rev 2.8 - 03-Feb-19 : Added audio registers flags (Álvaro Cuesta)
            ;* Rev 2.9 - 28-Feb-20 : Added utility rP1 constants
            ;* Rev 3.0 - 27-Aug-20 : Register ordering, byte-based sizes, OAM additions, general cleanup (Blitter Object)
            ;* Rev 4.0 - 03-May-21 : Updated to use RGBDS 0.5.0 syntax, changed IEF_LCDC to IEF_STAT (Eievui)

    IF __RGBDS_MAJOR__ == 0 && __RGBDS_MINOR__ < 5
    FAIL "This version of 'hardware.inc' requires RGBDS version 0.5.0 or later."
    ENDC

    ; If all of these are already defined, don't do it again.

    IF !DEF(HARDWARE_INC)
    DEF HARDWARE_INC EQU 1

    MACRO rev_Check_hardware_inc
            ;NOTE: REVISION NUMBER CHANGES MUST BE ADDED
            ;TO SECOND PARAMETER IN FOLLOWING LINE.
    IF  \1 > 4.0 ;PUT REVISION NUMBER HERE
    WARN    "Version \1 or later of 'hardware.inc' is required."
    ENDC
            ENDM

    DEF _VRAM        EQU $8000 ; $8000->$9FFF
    DEF _VRAM8000    EQU _VRAM
    DEF _VRAM8800    EQU _VRAM+$800
    DEF _VRAM9000    EQU _VRAM+$1000
    DEF _SCRN0       EQU $9800 ; $9800->$9BFF
    DEF _SCRN1       EQU $9C00 ; $9C00->$9FFF
    DEF _SRAM        EQU $A000 ; $A000->$BFFF
    DEF _RAM         EQU $C000 ; $C000->$CFFF / $C000->$DFFF
    DEF _RAMBANK     EQU $D000 ; $D000->$DFFF
    DEF _OAMRAM      EQU $FE00 ; $FE00->$FE9F
    DEF _IO          EQU $FF00 ; $FF00->$FF7F,$FFFF
    DEF _AUD3WAVERAM EQU $FF30 ; $FF30->$FF3F
    DEF _HRAM        EQU $FF80 ; $FF80->$FFFE

    ; *** MBC5 Equates ***

    DEF rRAMG        EQU $0000 ; $0000->$1fff
    DEF rROMB0       EQU $2000 ; $2000->$2fff
    DEF rROMB1       EQU $3000 ; $3000->$3fff - If more than 256 ROM banks are present.
    DEF rRAMB        EQU $4000 ; $4000->$5fff - Bit 3 enables rumble (if present)


            ;***************************************************************************
    ;*
    ;* Custom registers
            ;*
    ;***************************************************************************

    ; --
    ; -- P1 ($FF00)
            ; -- Register for reading joy pad info. (R/W)
    ; --
    DEF rP1 EQU $FF00

    DEF P1F_5 EQU %00100000 ; P15 out port, set to 0 to get buttons
    DEF P1F_4 EQU %00010000 ; P14 out port, set to 0 to get dpad
    DEF P1F_3 EQU %00001000 ; P13 in port
    DEF P1F_2 EQU %00000100 ; P12 in port
    DEF P1F_1 EQU %00000010 ; P11 in port
    DEF P1F_0 EQU %00000001 ; P10 in port

    DEF P1F_GET_DPAD EQU P1F_5
    DEF P1F_GET_BTN  EQU P1F_4
    DEF P1F_GET_NONE EQU P1F_4 | P1F_5


    ; --
    ; -- SB ($FF01)
            ; -- Serial Transfer Data (R/W)
            ; --
    DEF rSB EQU $FF01


            ; --
    ; -- SC ($FF02)
            ; -- Serial I/O Control (R/W)
            ; --
    DEF rSC EQU $FF02


            ; --
    ; -- DIV ($FF04)
            ; -- Divider register (R/W)
            ; --
    DEF rDIV EQU $FF04


            ; --
    ; -- TIMA ($FF05)
            ; -- Timer counter (R/W)
            ; --
    DEF rTIMA EQU $FF05


            ; --
    ; -- TMA ($FF06)
            ; -- Timer modulo (R/W)
            ; --
    DEF rTMA EQU $FF06


            ; --
    ; -- TAC ($FF07)
            ; -- Timer control (R/W)
            ; --
    DEF rTAC EQU $FF07

    DEF TACF_START  EQU %00000100
    DEF TACF_STOP   EQU %00000000
    DEF TACF_4KHZ   EQU %00000000
    DEF TACF_16KHZ  EQU %00000011
    DEF TACF_65KHZ  EQU %00000010
    DEF TACF_262KHZ EQU %00000001


    ; --
    ; -- IF ($FF0F)
            ; -- Interrupt Flag (R/W)
            ; --
    DEF rIF EQU $FF0F


            ; --
    ; -- AUD1SWEEP/NR10 ($FF10)
            ; -- Sweep register (R/W)
            ; --
    ; -- Bit 6-4 - Sweep Time
            ; -- Bit 3   - Sweep Increase/Decrease
    ; --           0: Addition    (frequency increases???)
            ; --           1: Subtraction (frequency increases???)
            ; -- Bit 2-0 - Number of sweep shift (# 0-7)
            ; -- Sweep Time: (n*7.8ms)
    ; --
    DEF rNR10 EQU $FF10
    DEF rAUD1SWEEP EQU rNR10

    DEF AUD1SWEEP_UP   EQU %00000000
    DEF AUD1SWEEP_DOWN EQU %00001000


    ; --
    ; -- AUD1LEN/NR11 ($FF11)
            ; -- Sound length/Wave pattern duty (R/W)
            ; --
    ; -- Bit 7-6 - Wave Pattern Duty (00:12.5% 01:25% 10:50% 11:75%)
            ; -- Bit 5-0 - Sound length data (# 0-63)
            ; --
    DEF rNR11 EQU $FF11
    DEF rAUD1LEN EQU rNR11


            ; --
    ; -- AUD1ENV/NR12 ($FF12)
            ; -- Envelope (R/W)
            ; --
    ; -- Bit 7-4 - Initial value of envelope
            ; -- Bit 3   - Envelope UP/DOWN
    ; --           0: Decrease
    ; --           1: Range of increase
    ; -- Bit 2-0 - Number of envelope sweep (# 0-7)
            ; --
    DEF rNR12 EQU $FF12
    DEF rAUD1ENV EQU rNR12


            ; --
    ; -- AUD1LOW/NR13 ($FF13)
            ; -- Frequency low byte (W)
            ; --
    DEF rNR13 EQU $FF13
    DEF rAUD1LOW EQU rNR13


            ; --
    ; -- AUD1HIGH/NR14 ($FF14)
            ; -- Frequency high byte (W)
            ; --
    ; -- Bit 7   - Initial (when set, sound restarts)
            ; -- Bit 6   - Counter/consecutive selection
            ; -- Bit 2-0 - Frequency's higher 3 bits
    ; --
    DEF rNR14 EQU $FF14
    DEF rAUD1HIGH EQU rNR14


            ; --
    ; -- AUD2LEN/NR21 ($FF16)
            ; -- Sound Length; Wave Pattern Duty (R/W)
            ; --
    ; -- see AUD1LEN for info
    ; --
    DEF rNR21 EQU $FF16
    DEF rAUD2LEN EQU rNR21


            ; --
    ; -- AUD2ENV/NR22 ($FF17)
            ; -- Envelope (R/W)
            ; --
    ; -- see AUD1ENV for info
    ; --
    DEF rNR22 EQU $FF17
    DEF rAUD2ENV EQU rNR22


            ; --
    ; -- AUD2LOW/NR23 ($FF18)
            ; -- Frequency low byte (W)
            ; --
    DEF rNR23 EQU $FF18
    DEF rAUD2LOW EQU rNR23


            ; --
    ; -- AUD2HIGH/NR24 ($FF19)
            ; -- Frequency high byte (W)
            ; --
    ; -- see AUD1HIGH for info
    ; --
    DEF rNR24 EQU $FF19
    DEF rAUD2HIGH EQU rNR24


            ; --
    ; -- AUD3ENA/NR30 ($FF1A)
            ; -- Sound on/off (R/W)
            ; --
    ; -- Bit 7   - Sound ON/OFF (1=ON,0=OFF)
            ; --
    DEF rNR30 EQU $FF1A
    DEF rAUD3ENA EQU rNR30


            ; --
    ; -- AUD3LEN/NR31 ($FF1B)
            ; -- Sound length (R/W)
            ; --
    ; -- Bit 7-0 - Sound length
            ; --
    DEF rNR31 EQU $FF1B
    DEF rAUD3LEN EQU rNR31


            ; --
    ; -- AUD3LEVEL/NR32 ($FF1C)
            ; -- Select output level
    ; --
    ; -- Bit 6-5 - Select output level
    ; --           00: 0/1 (mute)
    ; --           01: 1/1
    ; --           10: 1/2
    ; --           11: 1/4
    ; --
    DEF rNR32 EQU $FF1C
    DEF rAUD3LEVEL EQU rNR32


            ; --
    ; -- AUD3LOW/NR33 ($FF1D)
            ; -- Frequency low byte (W)
            ; --
    ; -- see AUD1LOW for info
    ; --
    DEF rNR33 EQU $FF1D
    DEF rAUD3LOW EQU rNR33


            ; --
    ; -- AUD3HIGH/NR34 ($FF1E)
            ; -- Frequency high byte (W)
            ; --
    ; -- see AUD1HIGH for info
    ; --
    DEF rNR34 EQU $FF1E
    DEF rAUD3HIGH EQU rNR34


            ; --
    ; -- AUD4LEN/NR41 ($FF20)
            ; -- Sound length (R/W)
            ; --
    ; -- Bit 5-0 - Sound length data (# 0-63)
            ; --
    DEF rNR41 EQU $FF20
    DEF rAUD4LEN EQU rNR41


            ; --
    ; -- AUD4ENV/NR42 ($FF21)
            ; -- Envelope (R/W)
            ; --
    ; -- see AUD1ENV for info
    ; --
    DEF rNR42 EQU $FF21
    DEF rAUD4ENV EQU rNR42


            ; --
    ; -- AUD4POLY/NR43 ($FF22)
            ; -- Polynomial counter (R/W)
            ; --
    ; -- Bit 7-4 - Selection of the shift clock frequency of the (scf)
            ; --           polynomial counter (0000-1101)
            ; --           freq=drf*1/2^scf (not sure)
            ; -- Bit 3 -   Selection of the polynomial counter's step
    ; --           0: 15 steps
    ; --           1: 7 steps
    ; -- Bit 2-0 - Selection of the dividing ratio of frequencies (drf)
            ; --           000: f/4   001: f/8   010: f/16  011: f/24
    ; --           100: f/32  101: f/40  110: f/48  111: f/56  (f=4.194304 Mhz)
    ; --
    DEF rNR43 EQU $FF22
    DEF rAUD4POLY EQU rNR43


            ; --
    ; -- AUD4GO/NR44 ($FF23)
            ; --
    ; -- Bit 7 -   Inital
    ; -- Bit 6 -   Counter/consecutive selection
            ; --
    DEF rNR44 EQU $FF23
    DEF rAUD4GO EQU rNR44


            ; --
    ; -- AUDVOL/NR50 ($FF24)
            ; -- Channel control / ON-OFF / Volume (R/W)
            ; --
    ; -- Bit 7   - Vin->SO2 ON/OFF (Vin??)
            ; -- Bit 6-4 - SO2 output level (volume) (# 0-7)
    ; -- Bit 3   - Vin->SO1 ON/OFF (Vin??)
            ; -- Bit 2-0 - SO1 output level (volume) (# 0-7)
    ; --
    DEF rNR50 EQU $FF24
    DEF rAUDVOL EQU rNR50

    DEF AUDVOL_VIN_LEFT  EQU %10000000 ; SO2
    DEF AUDVOL_VIN_RIGHT EQU %00001000 ; SO1


    ; --
    ; -- AUDTERM/NR51 ($FF25)
            ; -- Selection of Sound output terminal (R/W)
            ; --
    ; -- Bit 7   - Output sound 4 to SO2 terminal
    ; -- Bit 6   - Output sound 3 to SO2 terminal
    ; -- Bit 5   - Output sound 2 to SO2 terminal
    ; -- Bit 4   - Output sound 1 to SO2 terminal
    ; -- Bit 3   - Output sound 4 to SO1 terminal
    ; -- Bit 2   - Output sound 3 to SO1 terminal
    ; -- Bit 1   - Output sound 2 to SO1 terminal
    ; -- Bit 0   - Output sound 0 to SO1 terminal
    ; --
    DEF rNR51 EQU $FF25
    DEF rAUDTERM EQU rNR51

            ; SO2
    DEF AUDTERM_4_LEFT  EQU %10000000
    DEF AUDTERM_3_LEFT  EQU %01000000
    DEF AUDTERM_2_LEFT  EQU %00100000
    DEF AUDTERM_1_LEFT  EQU %00010000
    ; SO1
    DEF AUDTERM_4_RIGHT EQU %00001000
    DEF AUDTERM_3_RIGHT EQU %00000100
    DEF AUDTERM_2_RIGHT EQU %00000010
    DEF AUDTERM_1_RIGHT EQU %00000001


    ; --
    ; -- AUDENA/NR52 ($FF26)
            ; -- Sound on/off (R/W)
            ; --
    ; -- Bit 7   - All sound on/off (sets all audio regs to 0!)
            ; -- Bit 3   - Sound 4 ON flag (read only)
            ; -- Bit 2   - Sound 3 ON flag (read only)
            ; -- Bit 1   - Sound 2 ON flag (read only)
            ; -- Bit 0   - Sound 1 ON flag (read only)
            ; --
    DEF rNR52 EQU $FF26
    DEF rAUDENA EQU rNR52

    DEF AUDENA_ON    EQU %10000000
    DEF AUDENA_OFF   EQU %00000000  ; sets all audio regs to 0!


    ; --
    ; -- LCDC ($FF40)
            ; -- LCD Control (R/W)
            ; --
    DEF rLCDC EQU $FF40

    DEF LCDCF_OFF     EQU %00000000 ; LCD Control Operation
    DEF LCDCF_ON      EQU %10000000 ; LCD Control Operation
    DEF LCDCF_WIN9800 EQU %00000000 ; Window Tile Map Display Select
    DEF LCDCF_WIN9C00 EQU %01000000 ; Window Tile Map Display Select
    DEF LCDCF_WINOFF  EQU %00000000 ; Window Display
    DEF LCDCF_WINON   EQU %00100000 ; Window Display
    DEF LCDCF_BG8800  EQU %00000000 ; BG & Window Tile Data Select
    DEF LCDCF_BG8000  EQU %00010000 ; BG & Window Tile Data Select
    DEF LCDCF_BG9800  EQU %00000000 ; BG Tile Map Display Select
    DEF LCDCF_BG9C00  EQU %00001000 ; BG Tile Map Display Select
    DEF LCDCF_OBJ8    EQU %00000000 ; OBJ Construction
    DEF LCDCF_OBJ16   EQU %00000100 ; OBJ Construction
    DEF LCDCF_OBJOFF  EQU %00000000 ; OBJ Display
    DEF LCDCF_OBJON   EQU %00000010 ; OBJ Display
    DEF LCDCF_BGOFF   EQU %00000000 ; BG Display
    DEF LCDCF_BGON    EQU %00000001 ; BG Display
            ; "Window Character Data Select" follows BG


            ; --
    ; -- STAT ($FF41)
            ; -- LCDC Status   (R/W)
            ; --
    DEF rSTAT EQU $FF41

    DEF STATF_LYC     EQU  %01000000 ; LYC=LY Coincidence (Selectable)
    DEF STATF_MODE10  EQU  %00100000 ; Mode 10
    DEF STATF_MODE01  EQU  %00010000 ; Mode 01 (V-Blank)
    DEF STATF_MODE00  EQU  %00001000 ; Mode 00 (H-Blank)
    DEF STATF_LYCF    EQU  %00000100 ; Coincidence Flag
    DEF STATF_HBL     EQU  %00000000 ; H-Blank
    DEF STATF_VBL     EQU  %00000001 ; V-Blank
    DEF STATF_OAM     EQU  %00000010 ; OAM-RAM is used by system
    DEF STATF_LCD     EQU  %00000011 ; Both OAM and VRAM used by system
    DEF STATF_BUSY    EQU  %00000010 ; When set, VRAM access is unsafe


    ; --
    ; -- SCY ($FF42)
            ; -- Scroll Y (R/W)
            ; --
    DEF rSCY EQU $FF42


            ; --
    ; -- SCX ($FF43)
            ; -- Scroll X (R/W)
            ; --
    DEF rSCX EQU $FF43


            ; --
    ; -- LY ($FF44)
            ; -- LCDC Y-Coordinate (R)
            ; --
    ; -- Values range from 0->153. 144->153 is the VBlank period.
    ; --
    DEF rLY EQU $FF44


            ; --
    ; -- LYC ($FF45)
            ; -- LY Compare (R/W)
            ; --
    ; -- When LY==LYC, STATF_LYCF will be set in STAT
            ; --
    DEF rLYC EQU $FF45


            ; --
    ; -- DMA ($FF46)
            ; -- DMA Transfer and Start Address (W)
            ; --
    DEF rDMA EQU $FF46


            ; --
    ; -- BGP ($FF47)
            ; -- BG Palette Data (W)
            ; --
    ; -- Bit 7-6 - Intensity for %11
    ; -- Bit 5-4 - Intensity for %10
    ; -- Bit 3-2 - Intensity for %01
    ; -- Bit 1-0 - Intensity for %00
    ; --
    DEF rBGP EQU $FF47


            ; --
    ; -- OBP0 ($FF48)
            ; -- Object Palette 0 Data (W)
            ; --
    ; -- See BGP for info
    ; --
    DEF rOBP0 EQU $FF48


            ; --
    ; -- OBP1 ($FF49)
            ; -- Object Palette 1 Data (W)
            ; --
    ; -- See BGP for info
    ; --
    DEF rOBP1 EQU $FF49


            ; --
    ; -- WY ($FF4A)
            ; -- Window Y Position (R/W)
            ; --
    ; -- 0 <= WY <= 143
    ; -- When WY = 0, the window is displayed from the top edge of the LCD screen.
    ; --
    DEF rWY EQU $FF4A


            ; --
    ; -- WX ($FF4B)
            ; -- Window X Position (R/W)
            ; --
    ; -- 7 <= WX <= 166
    ; -- When WX = 7, the window is displayed from the left edge of the LCD screen.
    ; -- Values of 0-6 and 166 are unreliable due to hardware bugs.
    ; --
    DEF rWX EQU $FF4B


            ; --
    ; -- SPEED ($FF4D)
            ; -- Select CPU Speed (R/W)
            ; --
    DEF rKEY1 EQU $FF4D
    DEF rSPD  EQU rKEY1

    DEF KEY1F_DBLSPEED EQU %10000000 ; 0=Normal Speed, 1=Double Speed (R)
    DEF KEY1F_PREPARE  EQU %00000001 ; 0=No, 1=Prepare (R/W)


            ; --
    ; -- VBK ($FF4F)
            ; -- Select Video RAM Bank (R/W)
            ; --
    ; -- Bit 0 - Bank Specification (0: Specify Bank 0; 1: Specify Bank 1)
    ; --
    DEF rVBK EQU $FF4F


            ; --
    ; -- HDMA1 ($FF51)
            ; -- High byte for Horizontal Blanking/General Purpose DMA source address (W)
            ; -- CGB Mode Only
    ; --
    DEF rHDMA1 EQU $FF51


            ; --
    ; -- HDMA2 ($FF52)
            ; -- Low byte for Horizontal Blanking/General Purpose DMA source address (W)
            ; -- CGB Mode Only
    ; --
    DEF rHDMA2 EQU $FF52


            ; --
    ; -- HDMA3 ($FF53)
            ; -- High byte for Horizontal Blanking/General Purpose DMA destination address (W)
            ; -- CGB Mode Only
    ; --
    DEF rHDMA3 EQU $FF53


            ; --
    ; -- HDMA4 ($FF54)
            ; -- Low byte for Horizontal Blanking/General Purpose DMA destination address (W)
            ; -- CGB Mode Only
    ; --
    DEF rHDMA4 EQU $FF54


            ; --
    ; -- HDMA5 ($FF55)
            ; -- Transfer length (in tiles minus 1)/mode/start for Horizontal Blanking, General Purpose DMA (R/W)
            ; -- CGB Mode Only
    ; --
    DEF rHDMA5 EQU $FF55

    DEF HDMA5F_MODE_GP  EQU %00000000 ; General Purpose DMA (W)
    DEF HDMA5F_MODE_HBL EQU %10000000 ; HBlank DMA (W)

            ; -- Once DMA has started, use HDMA5F_BUSY to check when the transfer is complete
    DEF HDMA5F_BUSY EQU %10000000 ; 0=Busy (DMA still in progress), 1=Transfer complete (R)


            ; --
    ; -- RP ($FF56)
            ; -- Infrared Communications Port (R/W)
            ; -- CGB Mode Only
    ; --
    DEF rRP EQU $FF56

    DEF RPF_ENREAD   EQU %11000000
    DEF RPF_DATAIN   EQU %00000010 ; 0=Receiving IR Signal, 1=Normal
    DEF RPF_WRITE_HI EQU %00000001
    DEF RPF_WRITE_LO EQU %00000000


    ; --
    ; -- BCPS ($FF68)
            ; -- Background Color Palette Specification (R/W)
            ; --
    DEF rBCPS EQU $FF68

    DEF BCPSF_AUTOINC EQU %10000000 ; Auto Increment (0=Disabled, 1=Increment after Writing)


            ; --
    ; -- BCPD ($FF69)
            ; -- Background Color Palette Data (R/W)
            ; --
    DEF rBCPD EQU $FF69


            ; --
    ; -- OCPS ($FF6A)
            ; -- Object Color Palette Specification (R/W)
            ; --
    DEF rOCPS EQU $FF6A

    DEF OCPSF_AUTOINC EQU %10000000 ; Auto Increment (0=Disabled, 1=Increment after Writing)


            ; --
    ; -- OCPD ($FF6B)
            ; -- Object Color Palette Data (R/W)
            ; --
    DEF rOCPD EQU $FF6B


            ; --
    ; -- SMBK/SVBK ($FF70)
            ; -- Select Main RAM Bank (R/W)
            ; --
    ; -- Bit 2-0 - Bank Specification (0,1: Specify Bank 1; 2-7: Specify Banks 2-7)
    ; --
    DEF rSVBK EQU $FF70
    DEF rSMBK EQU rSVBK


            ; --
    ; -- PCM12 ($FF76)
            ; -- Sound channel 1&2 PCM amplitude (R)
            ; --
    ; -- Bit 7-4 - Copy of sound channel 2's PCM amplitude
    ; -- Bit 3-0 - Copy of sound channel 1's PCM amplitude
    ; --
    DEF rPCM12 EQU $FF76


            ; --
    ; -- PCM34 ($FF77)
            ; -- Sound channel 3&4 PCM amplitude (R)
            ; --
    ; -- Bit 7-4 - Copy of sound channel 4's PCM amplitude
    ; -- Bit 3-0 - Copy of sound channel 3's PCM amplitude
    ; --
    DEF rPCM34 EQU $FF77


            ; --
    ; -- IE ($FFFF)
            ; -- Interrupt Enable (R/W)
            ; --
    DEF rIE EQU $FFFF

    DEF IEF_HILO   EQU %00010000 ; Transition from High to Low of Pin number P10-P13
    DEF IEF_SERIAL EQU %00001000 ; Serial I/O transfer end
    DEF IEF_TIMER  EQU %00000100 ; Timer Overflow
    DEF IEF_STAT   EQU %00000010 ; STAT
    DEF IEF_VBLANK EQU %00000001 ; V-Blank


    ;***************************************************************************
    ;*
    ;* Flags common to multiple sound channels
            ;*
    ;***************************************************************************

    ; --
    ; -- Square wave duty cycle
            ; --
    ; -- Can be used with AUD1LEN and AUD2LEN
    ; -- See AUD1LEN for more info
            ; --
    DEF AUDLEN_DUTY_12_5    EQU %00000000 ; 12.5%
    DEF AUDLEN_DUTY_25      EQU %01000000 ; 25%
    DEF AUDLEN_DUTY_50      EQU %10000000 ; 50%
    DEF AUDLEN_DUTY_75      EQU %11000000 ; 75%


    ; --
    ; -- Audio envelope flags
    ; --
    ; -- Can be used with AUD1ENV, AUD2ENV, AUD4ENV
    ; -- See AUD1ENV for more info
            ; --
    DEF AUDENV_UP           EQU %00001000
    DEF AUDENV_DOWN         EQU %00000000


    ; --
    ; -- Audio trigger flags
    ; --
    ; -- Can be used with AUD1HIGH, AUD2HIGH, AUD3HIGH
    ; -- See AUD1HIGH for more info
            ; --

    DEF AUDHIGH_RESTART     EQU %10000000
    DEF AUDHIGH_LENGTH_ON   EQU %01000000
    DEF AUDHIGH_LENGTH_OFF  EQU %00000000


    ;***************************************************************************
    ;*
    ;* CPU values on bootup (a=type, b=qualifier)
            ;*
    ;***************************************************************************

    DEF BOOTUP_A_DMG    EQU $01 ; Dot Matrix Game
    DEF BOOTUP_A_CGB    EQU $11 ; Color GameBoy
    DEF BOOTUP_A_MGB    EQU $FF ; Mini GameBoy (Pocket GameBoy)

            ; if a=BOOTUP_A_CGB, bit 0 in b can be checked to determine if real CGB or
    ; other system running in GBC mode
    DEF BOOTUP_B_CGB    EQU %00000000
    DEF BOOTUP_B_AGB    EQU %00000001   ; GBA, GBA SP, Game Boy Player, or New GBA SP


    ;***************************************************************************
    ;*
    ;* Cart related
            ;*
    ;***************************************************************************

    ; $0143 Color GameBoy compatibility code
    DEF CART_COMPATIBLE_DMG     EQU $00
    DEF CART_COMPATIBLE_DMG_GBC EQU $80
    DEF CART_COMPATIBLE_GBC     EQU $C0

            ; $0146 GameBoy/Super GameBoy indicator
    DEF CART_INDICATOR_GB       EQU $00
    DEF CART_INDICATOR_SGB      EQU $03

            ; $0147 Cartridge type
    DEF CART_ROM                     EQU $00
    DEF CART_ROM_MBC1                EQU $01
    DEF CART_ROM_MBC1_RAM            EQU $02
    DEF CART_ROM_MBC1_RAM_BAT        EQU $03
    DEF CART_ROM_MBC2                EQU $05
    DEF CART_ROM_MBC2_BAT            EQU $06
    DEF CART_ROM_RAM                 EQU $08
    DEF CART_ROM_RAM_BAT             EQU $09
    DEF CART_ROM_MMM01               EQU $0B
    DEF CART_ROM_MMM01_RAM           EQU $0C
    DEF CART_ROM_MMM01_RAM_BAT       EQU $0D
    DEF CART_ROM_MBC3_BAT_RTC        EQU $0F
    DEF CART_ROM_MBC3_RAM_BAT_RTC    EQU $10
    DEF CART_ROM_MBC3                EQU $11
    DEF CART_ROM_MBC3_RAM            EQU $12
    DEF CART_ROM_MBC3_RAM_BAT        EQU $13
    DEF CART_ROM_MBC5                EQU $19
    DEF CART_ROM_MBC5_BAT            EQU $1A
    DEF CART_ROM_MBC5_RAM_BAT        EQU $1B
    DEF CART_ROM_MBC5_RUMBLE         EQU $1C
    DEF CART_ROM_MBC5_RAM_RUMBLE     EQU $1D
    DEF CART_ROM_MBC5_RAM_BAT_RUMBLE EQU $1E
    DEF CART_ROM_MBC7_RAM_BAT_GYRO   EQU $22
    DEF CART_ROM_POCKET_CAMERA       EQU $FC
    DEF CART_ROM_BANDAI_TAMA5        EQU $FD
    DEF CART_ROM_HUDSON_HUC3         EQU $FE
    DEF CART_ROM_HUDSON_HUC1         EQU $FF

            ; $0148 ROM size
    ; these are kilobytes
    DEF CART_ROM_32KB   EQU $00 ; 2 banks
    DEF CART_ROM_64KB   EQU $01 ; 4 banks
    DEF CART_ROM_128KB  EQU $02 ; 8 banks
    DEF CART_ROM_256KB  EQU $03 ; 16 banks
    DEF CART_ROM_512KB  EQU $04 ; 32 banks
    DEF CART_ROM_1024KB EQU $05 ; 64 banks
    DEF CART_ROM_2048KB EQU $06 ; 128 banks
    DEF CART_ROM_4096KB EQU $07 ; 256 banks
    DEF CART_ROM_8192KB EQU $08 ; 512 banks
    DEF CART_ROM_1152KB EQU $52 ; 72 banks
    DEF CART_ROM_1280KB EQU $53 ; 80 banks
    DEF CART_ROM_1536KB EQU $54 ; 96 banks

    ; $0149 SRAM size
    ; these are kilobytes
    DEF CART_SRAM_NONE  EQU 0
    DEF CART_SRAM_2KB   EQU 1 ; 1 incomplete bank
    DEF CART_SRAM_8KB   EQU 2 ; 1 bank
    DEF CART_SRAM_32KB  EQU 3 ; 4 banks
    DEF CART_SRAM_128KB EQU 4 ; 16 banks

    DEF CART_SRAM_ENABLE  EQU $0A
    DEF CART_SRAM_DISABLE EQU $00

            ; $014A Destination code
    DEF CART_DEST_JAPANESE     EQU $00
    DEF CART_DEST_NON_JAPANESE EQU $01


            ;***************************************************************************
    ;*
    ;* Keypad related
            ;*
    ;***************************************************************************

    DEF PADF_DOWN   EQU $80
    DEF PADF_UP     EQU $40
    DEF PADF_LEFT   EQU $20
    DEF PADF_RIGHT  EQU $10
    DEF PADF_START  EQU $08
    DEF PADF_SELECT EQU $04
    DEF PADF_B      EQU $02
    DEF PADF_A      EQU $01

    DEF PADB_DOWN   EQU $7
    DEF PADB_UP     EQU $6
    DEF PADB_LEFT   EQU $5
    DEF PADB_RIGHT  EQU $4
    DEF PADB_START  EQU $3
    DEF PADB_SELECT EQU $2
    DEF PADB_B      EQU $1
    DEF PADB_A      EQU $0


            ;***************************************************************************
    ;*
    ;* Screen related
            ;*
    ;***************************************************************************

    DEF SCRN_X    EQU 160 ; Width of screen in pixels
    DEF SCRN_Y    EQU 144 ; Height of screen in pixels
    DEF SCRN_X_B  EQU 20  ; Width of screen in bytes
    DEF SCRN_Y_B  EQU 18  ; Height of screen in bytes

    DEF SCRN_VX   EQU 256 ; Virtual width of screen in pixels
    DEF SCRN_VY   EQU 256 ; Virtual height of screen in pixels
    DEF SCRN_VX_B EQU 32  ; Virtual width of screen in bytes
    DEF SCRN_VY_B EQU 32  ; Virtual height of screen in bytes


            ;***************************************************************************
    ;*
    ;* OAM related
            ;*
    ;***************************************************************************

    ; OAM attributes
            ; each entry in OAM RAM is 4 bytes (sizeof_OAM_ATTRS)
    RSRESET
    DEF OAMA_Y              RB  1   ; y pos
    DEF OAMA_X              RB  1   ; x pos
    DEF OAMA_TILEID         RB  1   ; tile id
    DEF OAMA_FLAGS          RB  1   ; flags (see below)
    DEF sizeof_OAM_ATTRS    RB  0

    DEF OAM_COUNT           EQU 40  ; number of OAM entries in OAM RAM

    ; flags
    DEF OAMF_PRI        EQU %10000000 ; Priority
    DEF OAMF_YFLIP      EQU %01000000 ; Y flip
    DEF OAMF_XFLIP      EQU %00100000 ; X flip
    DEF OAMF_PAL0       EQU %00000000 ; Palette number; 0,1 (DMG)
    DEF OAMF_PAL1       EQU %00010000 ; Palette number; 0,1 (DMG)
    DEF OAMF_BANK0      EQU %00000000 ; Bank number; 0,1 (GBC)
    DEF OAMF_BANK1      EQU %00001000 ; Bank number; 0,1 (GBC)

    DEF OAMF_PALMASK    EQU %00000111 ; Palette (GBC)

    DEF OAMB_PRI        EQU 7 ; Priority
    DEF OAMB_YFLIP      EQU 6 ; Y flip
    DEF OAMB_XFLIP      EQU 5 ; X flip
    DEF OAMB_PAL1       EQU 4 ; Palette number; 0,1 (DMG)
    DEF OAMB_BANK1      EQU 3 ; Bank number; 0,1 (GBC)


    ;*
    ;* Nintendo scrolling logo
    ;* (Code won't work on a real GameBoy)
    ;* (if next lines are altered.)
    MACRO NINTENDO_LOGO
    DB  $CE,$ED,$66,$66,$CC,$0D,$00,$0B,$03,$73,$00,$83,$00,$0C,$00,$0D
    DB  $00,$08,$11,$1F,$88,$89,$00,$0E,$DC,$CC,$6E,$E6,$DD,$DD,$D9,$99
    DB  $BB,$BB,$67,$63,$6E,$0E,$EC,$CC,$DD,$DC,$99,$9F,$BB,$B9,$33,$3E
    ENDM

    ; Deprecated constants. Please avoid using.

    DEF IEF_LCDC   EQU %00000010 ; LCDC (see STAT)

    ENDC ;HARDWARE_INC
    "
            );

}
