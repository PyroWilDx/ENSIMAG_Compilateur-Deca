package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 *
 * @author gl47
 * @date 01/01/2024
 */
public class Not extends AbstractUnaryExpr {

    public Not(AbstractExpr operand) {
        super(operand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        compiler.getCondManager().incrNotCpt();

        getOperand().codeGenInst(compiler);

        compiler.getCondManager().decrNotCpt();
    }

    @Override
    protected void codeGenOpUnary(DecacCompiler compiler, GPRegister reg) {
        // Not Used
    }

    @Override
    protected String getOperatorName() {
        return "!";
    }
}
