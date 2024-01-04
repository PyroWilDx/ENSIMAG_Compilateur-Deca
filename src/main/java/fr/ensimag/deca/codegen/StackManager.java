package fr.ensimag.deca.codegen;

import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;

public class StackManager {

    private int stackSize;
    private int maxStackSize;
    private int gbVarCpt;
    private int vTableCpt;

    public StackManager() {
        this.stackSize = 0;
        this.maxStackSize = 0;
        this.gbVarCpt = 0;
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
        gbVarCpt++;
    }

    public void incrVTableCpt() {
        incrStackSize();
        vTableCpt++;
    }

    public RegisterOffset getGbOffsetAddr() {
        return new RegisterOffset(stackSize + 1, Register.GB);
    }

    public int getMaxStackSize() {
        return maxStackSize;
    }

    public int getAddSp() {
        return gbVarCpt + vTableCpt + 1;
    }

}
