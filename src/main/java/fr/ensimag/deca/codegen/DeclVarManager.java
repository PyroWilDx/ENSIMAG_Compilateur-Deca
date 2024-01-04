package fr.ensimag.deca.codegen;

import fr.ensimag.deca.context.Type;

public class DeclVarManager {

    private int gbVarCount;
    private Type currDeclVarType;

    public DeclVarManager() {
        this.gbVarCount = 0;
        this.currDeclVarType = null;
    }

    public void incrGbVarCount() {
        gbVarCount++;
    }

    public int getGbOffset() {
        return gbVarCount + StackManager.GLOBAL_BASE_OFFSET;
    }

    public void setCurrDeclVarType(Type value) {
        this.currDeclVarType = value;
    }

    public Type getCurrDeclVarType() {
        return currDeclVarType;
    }

}
