package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
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
        CondManager cM = compiler.getCondManager();

        cM.doOpCmp();
        super.codeGenInst(compiler);
        cM.exitOpCmp();
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        CondManager cM = compiler.getCondManager();

        compiler.addInstruction(new CMP(valReg, saveReg));
        Instruction opInst;
        if (cM.isDoingCond()) {
            if (branchLabel != null) {
                if (isNotInFalse) opInst = getBranchOpCmpInst(branchLabel);
                else opInst = getBranchInvOpCmpInst(branchLabel);
                compiler.addInstruction(opInst);
            }
        } else {
            RegManager rM = compiler.getRegManager();

            GPRegister gpReg = rM.getFreeReg();
            if (isNotInFalse) opInst = getOpCmpInst(gpReg);
            else opInst = getInvOpCmpInst(gpReg);
            compiler.addInstruction(opInst);
            rM.freeReg(gpReg);
        }
    }

    protected abstract Instruction getBranchInvOpCmpInst(Label bLabel);

    protected abstract Instruction getBranchOpCmpInst(Label bLabel);

    protected abstract Instruction getOpCmpInst(GPRegister gpReg);

    protected abstract Instruction getInvOpCmpInst(GPRegister gpReg);
}
