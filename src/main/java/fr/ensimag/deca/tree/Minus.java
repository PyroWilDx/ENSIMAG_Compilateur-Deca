package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Minus extends AbstractOpArith {
    public Minus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        // TODO (Not Implemented Yet)
    }

    @Override
    protected String getOperatorName() {
        return "-";
    }
    
}
