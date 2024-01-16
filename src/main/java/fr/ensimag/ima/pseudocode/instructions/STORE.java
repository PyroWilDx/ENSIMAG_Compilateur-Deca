package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.deca.codegen.GameBoyManager;
import fr.ensimag.ima.pseudocode.BinaryInstruction;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

import java.io.PrintStream;

/**
 * @author Ensimag
 * @date 01/01/2024
 */
public class STORE extends BinaryInstruction {
    public STORE(Register op1, DAddr op2) {
        super(op1, op2);
    }

    @Override
    public void displayOperandsGameBoy(PrintStream s) {

    }

    @Override
    public String getGameBoyAsm() {
        String gbAsm = "ld hl, SP";
        gbAsm += "\n\tadd hl, " + GameBoyManager.getImmToken() +
                ((RegisterOffset) getOperand2()).getOffset();
        gbAsm += "\n\tld [hl], " + getOperand1();
        return gbAsm;
    }
}
