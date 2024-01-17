package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

public class LOAD_INT extends BinaryInstructionDValToReg {
    public LOAD_INT(int i, GPRegister r) {
        super(new ImmediateInteger(i), r);
    }

    @Override
    public String getGameBoyAsm() {
        return "ld";
    }
}
