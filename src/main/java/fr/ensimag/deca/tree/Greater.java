package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.SGT;

/**
 *
 * @author gl47
 * @date 01/01/2024
 */
public class Greater extends AbstractOpIneq {

    public Greater(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
//        GPRegister reg = RegUtils.getUnusedReg();
//        compiler.addInstruction(new SGT(reg));
        // TODO (Not Implemented Yet)
    }

    @Override
    protected String getOperatorName() {
        return ">";
    }

}
