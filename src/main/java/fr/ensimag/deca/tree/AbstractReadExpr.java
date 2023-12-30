package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

/**
 * read...() statement.
 *
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractReadExpr extends AbstractExpr {

    public AbstractReadExpr() {
        super();
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        codeGenOpRead(compiler);
        compiler.addInstruction(new BOV(ErrorUtils.ioErrLabel));
        GPRegister reg = compiler.getRegManager().getFreeReg();
        compiler.addInstruction(new LOAD(Register.R1, reg));
        compiler.getRegManager().freeReg(reg);
        // Done
    }

    protected abstract void codeGenOpRead(DecacCompiler compiler);
}
