#include "Utils.decah"
class Color {
    boolean bit1 = 0;
    boolean bit2 = 0;
    void setBlack() {
        this.color = 0;
    }
    void setDark() {
        this.color = 1;
    }
    void setLight() {
        this.color = 2;
    }
    void setWhite() {
        this.color = 3;
    }

}

class GameBoy {
    protected int width = 160;
    protected int height = 144;
    protected int firstVRamAdress = 32768;
    protected utils = new Utils();

    int getWidth() {
        return this.width;
    }

    int getHeight() {
        return this.height;
    }
    void init () asm (
    "d"

    ); // TODO pour faire les trucs assembleurs de bases genre main loop et tout jsp
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
}