package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.CMP;

/**
 * @author gl47
 * @date 01/01/2024
 */
public abstract class AbstractOpCmp extends AbstractBinaryExpr {

    public AbstractOpCmp(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        getLeftOperand().codeGenInst(compiler);
        GPRegister lReg = RegUtils.getCurrReg();
        getRightOperand().codeGenInst(compiler);
        GPRegister rReg = RegUtils.getCurrReg();
        compiler.addInstruction(new CMP(lReg, rReg));
        RegUtils.freeReg(lReg);
        codeGenOpCmp(compiler, rReg);
        // TODO (Not Enough Registers) and (When Register is Freed ?)
    }

    protected abstract void codeGenOpCmp(DecacCompiler compiler, GPRegister reg);
}
