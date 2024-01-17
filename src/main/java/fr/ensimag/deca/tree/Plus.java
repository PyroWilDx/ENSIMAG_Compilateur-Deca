package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.ADD;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenOpArith(DecacCompiler compiler,
                                  DVal valReg, GPRegister saveReg) {
        compiler.addInstruction(new ADD(valReg, saveReg));
        // Done
    }

    @Override
    protected void codeGenOpArithGb(DecacCompiler compiler,
                                    DVal valReg, GPRegister saveReg) {
        compiler.addInstruction(new ADD(valReg, saveReg));
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }
}
