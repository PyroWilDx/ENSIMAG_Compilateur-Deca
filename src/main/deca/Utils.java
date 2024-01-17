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
            rts
            "
            ); // TODO VRAIMENT PAS SUR.
    void push(int addr, int value) asm(
            "
            ld bc, 49670
            ld de, 49664
            ld [bc], de
            "
            ); // TODO VRAIMENT PAS SÛR
}