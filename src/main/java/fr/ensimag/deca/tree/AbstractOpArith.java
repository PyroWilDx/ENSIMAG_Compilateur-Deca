package fr.ensimag.deca.tree;

import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;
import fr.ensimag.ima.pseudocode.instructions.PUSH;

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
        GPRegister lReg = RegUtils.getAndUseCurrReg();

        boolean pushed = false;
        if (RegUtils.isUsingAllRegs()) {
            compiler.addInstruction(new PUSH(lReg));
            RegUtils.freeReg(lReg);
            pushed = true;
        }

        getRightOperand().codeGenInst(compiler);
        GPRegister rReg = RegUtils.getAndUseCurrReg();

        if (pushed) {
            lReg = Register.R0;
            compiler.addInstruction(new LOAD(rReg, lReg));
            compiler.addInstruction(new POP(rReg));
        }

        codeGenOpArith(compiler, lReg, rReg);

        RegUtils.freeReg(lReg);
        RegUtils.freeReg(rReg);
        // Done
    }

    protected abstract void codeGenOpArith(DecacCompiler compiler,
                                           GPRegister lReg, GPRegister rReg);

}
