package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.UnaryInstruction;

import java.io.PrintStream;

public class DEC_REG extends UnaryInstruction {
    public DEC_REG(GPRegister op) {
        super(op);
    }

    @Override
    public String getGameBoyAsm() {
        return "dec";
    }
}
