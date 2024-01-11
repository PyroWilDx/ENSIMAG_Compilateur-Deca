package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class StackManager {

    private final boolean methodStack;
    private int stackSize;
    private int maxStackSize;
    private int varCpt;
    private int vTableCpt;

    public StackManager(boolean methodStack) {
        this.methodStack = methodStack;
        this.stackSize = 0;
        this.maxStackSize = 0;
        this.varCpt = 0;
        this.vTableCpt = 0;
    }

    public void incrStackSize() {
        stackSize++;
        if (stackSize > maxStackSize) {
            maxStackSize = stackSize;
        }
    }

    public void decrStackSize() {
        stackSize--;
    }

    public void incrGbVarCpt() {
        incrStackSize();
        varCpt++;
    }

    public void incrVTableCpt() {
        incrStackSize();
        vTableCpt++;
    }

    public RegisterOffset getOffsetAddr() {
        Register register = (methodStack) ? Register.LB : Register.GB;
        return new RegisterOffset(stackSize + 1, register);
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getAddSp() {
        return varCpt + vTableCpt;
    }

}
