package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.RegistersManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.ADD;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.POP;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Plus extends AbstractOpArith {
    public Plus(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        super.codeGenInst(compiler);
        GPRegister lastUsedReg = RegistersManager.getLastUsedReg();
        if (lastUsedReg.getNumber() == RegistersManager.nRegs) {
            compiler.addInstruction(new LOAD(lastUsedReg, Register.R0));
            compiler.addInstruction(new POP(lastUsedReg));
            compiler.addInstruction(new ADD(Register.R0, lastUsedReg));
        } else {
            GPRegister otherReg = Register.getR(lastUsedReg.getNumber() + 1);
            compiler.addInstruction(new ADD(otherReg, lastUsedReg));
        }
        // TODO
    }

    @Override
    protected String getOperatorName() {
        return "+";
    }
}
