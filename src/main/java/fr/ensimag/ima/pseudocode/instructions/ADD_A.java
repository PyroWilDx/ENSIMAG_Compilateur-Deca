package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

public class ADD_A extends BinaryInstructionDValToReg {
    public ADD_A(DVal op1, GPRegister op2) {
        super(op1, op2);

        assert (op2 == Register.A); // SUPER LA GAMEBOY JE KIFF
    }

    public ADD_A(int i, GPRegister op2) {
        this(new ImmediateInteger(i), op2);
    }

    @Override
    public String getGameBoyAsm() {
        return "add";
    }
}
