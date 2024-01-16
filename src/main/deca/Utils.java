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
            "LOAD addr,r0
            LOAD 0(r0),r0
            RTS"
            ) // TODO traduire en gameboy
    void push(int addr, int value) asm(
            "LOAD addr,r0
            LOAD -4lb, r1
            STORE r1, 0(r0)"
            ) // TODO traduire en gameboy
}