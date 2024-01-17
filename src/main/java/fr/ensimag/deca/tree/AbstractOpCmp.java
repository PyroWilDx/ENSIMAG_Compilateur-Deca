package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.GameBoyManager;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.ADD;
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
    protected void codeGenInstGb(DecacCompiler compiler) {
        CondManager cM = compiler.getCondManager();

        cM.doOpCmp();
        super.codeGenInstGb(compiler);
        cM.exitOpCmp();
    }

    @Override
    protected void codeGenOp(DecacCompiler compiler,
                             DVal valReg, GPRegister saveReg) {
        CondManager cM = compiler.getCondManager();

        compiler.addInstruction(new CMP(valReg, saveReg));
        if (cM.isDoingCond()) {
            if (isInTrue) compiler.addInstruction(getBranchOpCmpInst(branchLabel));
            else compiler.addInstruction(getBranchInvOpCmpInst(branchLabel));
        } else {
            if (isInTrue) compiler.addInstruction(getOpCmpInst(saveReg));
            else compiler.addInstruction(getInvOpCmpInst(saveReg));
        }
    }

    @Override
    protected void codeGenOpGb(DecacCompiler compiler,
                               DVal valReg, GPRegister saveReg) {
        CondManager cM = compiler.getCondManager();

        if (doAdd()) {
            if (!(valReg instanceof GPRegister)) {
                if (valReg instanceof ImmediateInteger) {
                    ImmediateInteger valRegImmInt = (ImmediateInteger) valReg;
                    valRegImmInt.addValue(1);
                } else if (valReg instanceof ImmediateFloat) {
                    ImmediateFloat valRegImmFloat = (ImmediateFloat) valReg;
                    valRegImmFloat.addValue(1);
                }
            } else {
                GPRegister gpReg = (GPRegister) valReg;
                compiler.addInstruction(new ADD(1, gpReg.getLowReg()));
            }
        }

        if (valReg instanceof GPRegister) {
            valReg = ((GPRegister) valReg).getLowReg();
        }
        compiler.addInstruction(new CMP(valReg, saveReg.getLowReg()));
        if (cM.isDoingCond()) {
            if (isInTrue) compiler.addInstruction(getBranchOpCmpInst(branchLabel));
            else compiler.addInstruction(getBranchInvOpCmpInst(branchLabel));
        } else {
            if (isInTrue) compiler.addInstruction(getOpCmpInst(saveReg.getLowReg()));
            else compiler.addInstruction(getInvOpCmpInst(saveReg.getLowReg()));
        }
    }

    protected boolean doAdd() {
        return false;
    }

    protected abstract Instruction getBranchInvOpCmpInst(Label bLabel);

    protected abstract Instruction getBranchOpCmpInst(Label bLabel);

    protected abstract Instruction getOpCmpInst(GPRegister gpReg);

    protected abstract Instruction getInvOpCmpInst(GPRegister gpReg);
}
