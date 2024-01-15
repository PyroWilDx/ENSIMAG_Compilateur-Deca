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

    public String getNameGameBoy() {
        return getName();
    }

    public abstract void displayOperandsGameBoy(PrintStream s);

    public void displayGameBoy(PrintStream s) {
        s.print(getName());
        displayOperandsGameBoy(s);
    }
}
