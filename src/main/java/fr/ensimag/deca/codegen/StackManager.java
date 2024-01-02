package fr.ensimag.deca.codegen;

public class StackManager {

    private int stackSize;
    private int maxStackSize;

    public StackManager() {
        stackSize = 2;
        maxStackSize = stackSize;
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

    public int getMaxStackSize() {
        return maxStackSize;
    }

}
