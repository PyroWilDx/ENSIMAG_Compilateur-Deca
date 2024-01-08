package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;

/**
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractStringLiteral extends AbstractLiteral {

    public abstract String getValue();

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // Done
    }
}
