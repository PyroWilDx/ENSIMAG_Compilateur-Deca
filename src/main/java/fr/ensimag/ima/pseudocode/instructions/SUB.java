package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class SUB extends BinaryInstructionDValToReg {
    public SUB(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public SUB(int i, GPRegister op2) {
        super(new ImmediateInteger(i), op2);
    }

    @Override
    public String getGameBoyAsm() {
        return "sub";
    }
}
