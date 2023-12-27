package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Or extends AbstractOpBool {

    public Or(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenOpBool(DecacCompiler compiler,
                                 GPRegister lReg, GPRegister rReg) {
        compiler.addInstruction(new ADD(lReg, rReg));
        // Done
    }

    @Override
    protected String getOperatorName() {
        return "||";
    }


}
