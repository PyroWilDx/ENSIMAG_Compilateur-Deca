package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

public class STORE_SP extends BinaryInstruction {

    public STORE_SP(DVal op1, Register op2) {
        super(op1, op2);

        assert (op2 == Register.SP);
    }

    public STORE_SP(int i, Register op2) {
        this(new ImmediateInteger(i), op2);
    }

    @Override
    public void displayOperandsGameBoy(PrintStream s) {
        s.print(" ");
        s.print(getOperand2());
        s.print(", ");
        s.print(getOperand1());
    }

    @Override
    public String getGameBoyAsm() {
        return "ld";
    }

}
