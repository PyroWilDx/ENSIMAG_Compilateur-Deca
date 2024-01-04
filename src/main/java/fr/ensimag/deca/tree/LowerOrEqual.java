package fr.ensimag.deca.tree;


import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Instruction;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.instructions.BGT;
import fr.ensimag.ima.pseudocode.instructions.BLE;
import fr.ensimag.ima.pseudocode.instructions.SGT;
import fr.ensimag.ima.pseudocode.instructions.SLE;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class LowerOrEqual extends AbstractOpIneq {
    public LowerOrEqual(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected Instruction getBranchInvOpCmpInst(Label bLabel) {
        return new BGT(bLabel);
    }

    @Override
    protected Instruction getBranchOpCmpInst(Label bLabel) {
        return new BLE(bLabel);
    }

    @Override
    protected Instruction getOpCmpInst(GPRegister gpReg) {
        return new SLE(gpReg);
    }

    @Override
    protected Instruction getInvOpCmpInst(GPRegister gpReg) {
        return new SGT(gpReg);
    }

    @Override
    protected String getOperatorName() {
        return "<=";
    }

}
