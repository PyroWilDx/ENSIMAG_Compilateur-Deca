package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.deca.codegen.RegUtils;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Divide extends AbstractOpArith {
    public Divide(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenOpArith(DecacCompiler compiler,
                                  DVal valReg, GPRegister saveReg) {
        if (getLeftOperand().getType().isInt() && getRightOperand().getType().isInt()) {
            compiler.addInstruction(new QUO(valReg, saveReg));
        } else {
            compiler.addInstruction(new DIV(valReg, saveReg));
        }
        compiler.addInstruction(new BOV(ErrorUtils.divBy0Label));
        // Done
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

}
