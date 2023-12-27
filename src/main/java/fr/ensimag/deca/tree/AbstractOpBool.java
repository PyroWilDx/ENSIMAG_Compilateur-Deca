package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractOpBool extends AbstractBinaryExpr {

    public AbstractOpBool(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getLeftOperand().codeGenInst(compiler);
        GPRegister lReg = RegUtils.getCurrReg();
        getRightOperand().codeGenInst(compiler);
        GPRegister rReg = RegUtils.getCurrReg();
        codeGenOpBool(compiler, lReg, rReg);
        RegUtils.freeReg(lReg);
        // TODO (Not Enough Registers)  and (When Register is Freed ?)
    }

    protected abstract void codeGenOpBool(DecacCompiler compiler,
                                          GPRegister lReg, GPRegister rReg);
}
