package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class FLOAT extends BinaryInstructionDValToReg {

    public FLOAT(DVal op1, GPRegister op2) {
        super(op1, op2);
    }

    @Override
    public String getGameBoyAsm() {
        return null; // Pas de float
    }
}
