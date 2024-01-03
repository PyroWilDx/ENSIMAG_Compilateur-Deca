package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGE;
import fr.ensimag.ima.pseudocode.instructions.BLT;
import fr.ensimag.ima.pseudocode.instructions.SLT;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Lower extends AbstractOpIneq {

    public Lower(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction getBranchInvOpCmpInst(Label bLabel) {
        return new BGE(bLabel);
    }

    @Override
    protected Instruction getBranchOpCmpInst(Label bLabel) {
        return new BLT(bLabel);
    }

    @Override
    protected Instruction getOpCmpInst(GPRegister reg) {
        return new SLT(reg);
    }


    @Override
    protected String getOperatorName() {
        return "<";
    }

}
