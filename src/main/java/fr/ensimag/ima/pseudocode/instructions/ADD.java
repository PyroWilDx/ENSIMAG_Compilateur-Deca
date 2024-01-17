package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.ImmediateInteger;

import java.io.PrintStream;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class ADD extends BinaryInstructionDValToReg {
    public ADD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public ADD(int i, GPRegister op2) {
        this(new ImmediateInteger(i), op2);
    }

    @Override
    public void displayOperandsGameBoy(PrintStream s) {
        s.print("ld a, " + getOperand2());
        s.println();
        s.print("\tadd a, " + getOperand1());
        s.println();
        s.print("\tld " + getOperand2() + ", a");
    }

    @Override
    public String getGameBoyAsm() {
        return "";
    }
}
