package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
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
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        codeGenOpArith(compiler, valReg, saveReg);
    }

    @Override
    protected void codeGenOpGb(DecacCompiler compiler,
                               DVal valReg, GPRegister saveReg) {
        codeGenOpArithGb(compiler, valReg, saveReg);
    }

    protected abstract void codeGenOpArith(DecacCompiler compiler,
                                           DVal valReg, GPRegister saveReg);

    protected abstract void codeGenOpArithGb(DecacCompiler compiler,
                                             DVal valReg, GPRegister saveReg);

}
