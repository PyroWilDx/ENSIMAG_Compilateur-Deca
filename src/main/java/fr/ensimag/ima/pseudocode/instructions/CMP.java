package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.deca.codegen.GameBoyManager;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

/**
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class CMP extends BinaryInstructionDValToReg {

    public CMP(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public CMP(int val, GPRegister op2) {
        this(new ImmediateInteger(val), op2);
    }

    @Override
    public void displayOperandsGameBoy(PrintStream s) {
        if (getOperand2() != Register.A) {
            s.print("ld a, " + getOperand2());
            s.println();
            s.print("\tcp a, " + getOperand1());
        } else {
            s.print("cp a, " + getOperand1());
        }
    }

    @Override
    public String getGameBoyAsm() {
        return "";
    }
}