package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;

/**
 * Arithmetic binary operations (+, -, /, ...)
 * 
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractOpArith extends AbstractBinaryExpr {

    public AbstractOpArith(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getLeftOperand().codeGenInst(compiler);
        GPRegister lReg = RegUtils.getCurrReg();
        getRightOperand().codeGenInst(compiler);
        GPRegister rReg = RegUtils.getCurrReg();
        codeGenOpArith(compiler, lReg, rReg);
        RegUtils.freeReg(lReg);
        // TODO (Not Enough Registers) and (When Register is Freed ?)
    }

    protected abstract void codeGenOpArith(DecacCompiler compiler,
                                           GPRegister lReg, GPRegister rReg);

}
