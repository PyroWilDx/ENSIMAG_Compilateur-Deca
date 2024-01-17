package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.BinaryInstructionDValToReg;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;

public class ADD_A extends BinaryInstructionDValToReg {
    public ADD_A(DVal op1, GPRegister op2) {
        super(op1, op2);

        assert (op2 == Register.A); // SUPER LA GAMEBOY JE KIFF
    }

    @Override
    public String getGameBoyAsm() {
        return "add";
    }
}
