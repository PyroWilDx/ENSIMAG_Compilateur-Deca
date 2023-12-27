package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SNE;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class NotEquals extends AbstractOpExactCmp {

    public NotEquals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenOpCmp(DecacCompiler compiler, GPRegister reg) {
        compiler.addInstruction(new SNE(reg));
    }

    @Override
    protected String getOperatorName() {
        return "!=";
    }

}
