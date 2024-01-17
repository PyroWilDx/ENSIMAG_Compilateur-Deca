package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

public class LOAD_SP extends BinaryInstruction {

    public LOAD_SP(Operand op1, Operand op2) {
        super(op1, op2);
    }

    public LOAD_SP(int i, Operand op2) {
        this(new ImmediateInteger(i), op2);
    }

    @Override
    public void displayOperandsGameBoy(PrintStream s) {
        s.print(" ");
        s.print(getOperand1());
    }

    @Override
    public String getGameBoyAsm() {
        return "ld SP,";
    }
}
