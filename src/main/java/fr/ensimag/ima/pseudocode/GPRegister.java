package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.codegen.GameBoyManager;

/**
 * General Purpose Register operand (R0, R1, ... R15).
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class GPRegister extends Register {
    public static final String[] gameBoyRegs = {"a", "hl", "bc", "de"};

    /**
     * @return the number of the register, e.g. 12 for R12.
     */
    public int getNumber() {
        return number;
    }

    private final int number;

    public GPRegister(String name, int number) {
        super((GameBoyManager.doCp && number < GameBoyManager.nRegs) ? gameBoyRegs[number] : name);
        this.number = number;
    }
}
