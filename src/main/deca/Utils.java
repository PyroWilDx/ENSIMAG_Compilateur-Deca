class Utils {
    int pow(int x, int exposant) {
        if (exposant == 0) {
            return 1;
        }
        int resultat = 1;
        while (exposant > 0) {
            if (exposant % 2 == 1) {
                resultat = resultat * x;
            }
            base = base * base;
            exposant = exposant / 2;
        }
    }
    int get(int addr) asm(
            "
            ld bc, -6[sp]
            ld [bc], bc
            rts
            "
            ) // TODO VRAIMENT PAS SUR.
    void push(int addr, int value) asm(
            "
            ld bc, -8[sp]
            ld de, -6[sp]
            ld [bc], de
            "
            ) // TODO VRAIMENT PAS SÛR
}