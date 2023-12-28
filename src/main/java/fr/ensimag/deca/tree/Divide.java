package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
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
                                  DVal lReg, GPRegister rReg) {
        // Version utilisant R1 faisant 6 cycles
//        compiler.addInstruction(new LOAD(rReg, Register.R1));
//        compiler.addInstruction(new LOAD(lReg, rReg));
//        compiler.addInstruction(new LOAD(Register.R1, Register.R0));

        // Version n'utilisant pas R1 faisant 8 cycles
        compiler.addInstruction(new PUSH(rReg));
        compiler.addInstruction(new LOAD(lReg, rReg));
        compiler.addInstruction(new POP(Register.R0));

        if (getLeftOperand().getType().isInt() && getRightOperand().getType().isInt()) {
            compiler.addInstruction(new QUO(Register.R0, rReg));
        } else {
            compiler.addInstruction(new DIV(Register.R0, rReg));
        }
        // Done
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

}
