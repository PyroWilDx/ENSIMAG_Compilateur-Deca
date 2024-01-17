package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

public class CMP_A extends BinaryInstructionDValToReg {

    public CMP_A(DVal op1, GPRegister op2) {
        super(op1, op2);

        assert (op2 == Register.A);
    }

    public CMP_A(int val, GPRegister op2) {
        this(new ImmediateInteger(val), op2);
    }

    @Override
    public String getGameBoyAsm() {
        return "cp";
    }
}
