package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Type;

public abstract class AbstractParam extends Tree {
    public abstract Type verifyDeclParam(DecacCompiler compiler) throws ContextualError;
    // Done
    // TODO
}
