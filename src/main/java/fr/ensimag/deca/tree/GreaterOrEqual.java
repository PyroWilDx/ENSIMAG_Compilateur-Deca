package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SGE;

/**
 * Operator "x >= y"
 *
 * @author gl47
 * @date 01/01/2024
 */
public class GreaterOrEqual extends AbstractOpIneq {

    public GreaterOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenOpCmp(DecacCompiler compiler, GPRegister reg) {
        compiler.addInstruction(new SGE(reg));
    }

    @Override
    protected String getOperatorName() {
        return ">=";
    }

}
