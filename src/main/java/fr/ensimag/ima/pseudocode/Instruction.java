package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

/**
 * IMA instruction.
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public abstract class Instruction {
    String getName() {
        return this.getClass().getSimpleName();
    }

    abstract void displayOperands(PrintStream s);

    void display(PrintStream s) {
        s.print(getName());
        displayOperands(s);
    }

    protected int nbGbAsmInst = 1;

    public String getGameBoyAsm() { // TODO (a enlever et mettre en abstract)
        return getName();
    }

//    public abstract String getGameBoyAsm();

    public abstract void displayOperandsGameBoy(PrintStream s);

    public void displayGameBoy(PrintStream s) {
//        while (nbGbAsmInst > 0) {
        String gbAsmInstName = getGameBoyAsm();
        if (gbAsmInstName == null) gbAsmInstName = getName();
        s.print(gbAsmInstName);
        displayOperandsGameBoy(s);
//            nbGbAsmInst--;
//        }
    }
}
