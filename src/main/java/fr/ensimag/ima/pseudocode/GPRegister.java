package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.codegen.GameBoy;

/**
 * General Purpose Register operand (R0, R1, ... R15).
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class GPRegister extends Register {
    //    private static final String[] gameBoyRegs = {"a", "b", "c", "d", "e", "h", "l"};
    private static final String[] gameBoyRegs = {"bc", "de", "hl"};

    /**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }

    private final int number;

    GPRegister(String name, int number) {
        super((GameBoy.doCp && number < GameBoy.N_OF_REGS) ? gameBoyRegs[number] : name);
        this.number = number;
    }
}
