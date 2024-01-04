package fr.ensimag.deca.codegen;

public class StackManager {

    public static int GLOBAL_BASE_OFFSET = 2;

    private int stackSize;
    private int maxStackSize;

    public StackManager() {
        stackSize = GLOBAL_BASE_OFFSET;
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
