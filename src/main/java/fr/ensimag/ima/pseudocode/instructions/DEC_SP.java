package fr.ensimag.ima.pseudocode.instructions;

import fr.ensimag.ima.pseudocode.Operand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.UnaryInstruction;

import java.io.PrintStream;

public class DEC_SP extends UnaryInstruction {

    public DEC_SP(Operand operand) {
        super(operand);

        assert (operand == Register.SP);
    }

    @Override
    public void displayOperandsGameBoy(PrintStream s) {
        s.print(" ");
        s.print(getOperand());
    }

    @Override
    public String getGameBoyAsm() {
        return "dec";
    }
}
