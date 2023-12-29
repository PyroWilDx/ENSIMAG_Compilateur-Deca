package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
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
        GPRegister reg = RegUtils.getFreeReg();
        compiler.addInstruction(new LOAD(Register.R1, reg));
        RegUtils.freeReg(reg);
        // TODO (Not Enough Registers)
    }

    protected abstract void codeGenOpRead(DecacCompiler compiler);
}
