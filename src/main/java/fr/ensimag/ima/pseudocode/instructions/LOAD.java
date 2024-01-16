package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.deca.codegen.GameBoy;
import fr.ensimag.ima.pseudocode.*;

import java.io.PrintStream;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class LOAD extends BinaryInstructionDValToReg {

    public LOAD(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    public LOAD(int i, GPRegister r) {
        this(new ImmediateInteger(i), r);
    }

    public LOAD(float f, GPRegister r) {
        this(new ImmediateFloat(f), r);
    }

    @Override
    public void displayOperandsGameBoy(PrintStream s) {
        if (!(getOperand1() instanceof RegisterOffset)) {
            super.displayOperandsGameBoy(s);
        }
    }

    @Override
    public String getGameBoyAsm() {
        if (getOperand1() instanceof RegisterOffset) {
            String gbAsm = "ld hl, SP";
            gbAsm += "\n\tadd hl, " + GameBoy.getImmToken() +
                    ((RegisterOffset) getOperand1()).getOffset();
            gbAsm += "\n\tld " + getOperand2() + ", [hl]";
            return gbAsm;
        }
        return "ld";
    }
}
