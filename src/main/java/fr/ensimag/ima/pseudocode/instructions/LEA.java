package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.deca.codegen.GameBoyManager;
import fr.ensimag.ima.pseudocode.BinaryInstructionDAddrToReg;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class LEA extends BinaryInstructionDAddrToReg {

    public LEA(DAddr op1, GPRegister op2) {
        super(op1, op2);
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
            gbAsm += "\n\tadd hl, " + GameBoyManager.getImmToken() +
                    ((RegisterOffset) getOperand1()).getOffset();
            gbAsm += "\n\tld [hl], " + getOperand2();
            return gbAsm;
        }
        return "ld";
    }
}
