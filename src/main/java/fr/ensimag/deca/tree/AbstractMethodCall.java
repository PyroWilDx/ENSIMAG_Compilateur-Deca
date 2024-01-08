package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

public abstract class AbstractMethodCall extends AbstractExpr {

    public abstract void codeGenMethodCall(DecacCompiler compiler);

}
