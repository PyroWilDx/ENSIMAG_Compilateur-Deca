package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.BNE;
import fr.ensimag.ima.pseudocode.instructions.SEQ;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Equals extends AbstractOpExactCmp {

    public Equals(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction getBranchInvOpCmpInst(Label bLabel) {
        return new BNE(bLabel);
    }

    @Override
    protected Instruction getBranchOpCmpInst(Label bLabel) {
        return new BEQ(bLabel);
    }

    @Override
    protected Instruction getOpCmpInst(GPRegister reg) {
        return new SEQ(reg);
    }

    @Override
    protected String getOperatorName() {
        return "==";
    }

}
