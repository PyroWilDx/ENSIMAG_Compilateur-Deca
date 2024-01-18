package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.ErrorManager;
import fr.ensimag.deca.codegen.GameBoyManager;
import fr.ensimag.ima.pseudocode.*;
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
        ErrorManager eM = compiler.getErrorManager();

        if (getLeftOperand().getType().isInt() && getRightOperand().getType().isInt()) {
            compiler.addInstruction(new QUO(valReg, saveReg));
            if (compiler.getCompilerOptions().doCheck()) {
                compiler.addInstruction(new BOV(eM.getDivBy0Label()));
            }
        } else {
            compiler.addInstruction(new DIV(valReg, saveReg));
        }
        // Done
    }

    @Override
    protected void codeGenOpArithGb(DecacCompiler compiler, DVal valReg, GPRegister saveReg) {
        // TODO (PUSH HL)
        CondManager cM = compiler.getCondManager();

        long id = cM.getUniqueId();
        Label startLabel = new Label("DivStart" + id);
        Label endLabel = new Label("DivEnd" + id);

        compiler.addInstruction(new LOAD_GEN(valReg, Register.A));
        compiler.addInstruction(new CMP_A(0, Register.A));
        compiler.addInstruction(new BEQ(endLabel));

        compiler.addInstruction(new LOAD_INT(0, Register.A));
        compiler.addInstruction(new LOAD_REG(saveReg, GPRegister.L));

        compiler.addLabel(startLabel);
        compiler.addInstruction(new LOAD_REG(Register.A, GPRegister.H));
        compiler.addInstruction(new LOAD_REG(GPRegister.L, Register.A));

        compiler.addInstruction(new SUB_A(valReg, Register.A));
        compiler.addInstruction(new BLT(endLabel));

        compiler.addInstruction(new LOAD_REG(Register.A, GPRegister.L));
        compiler.addInstruction(new LOAD_REG(GPRegister.H, Register.A));

        compiler.addInstruction(new ADD_A(1, Register.A));

        compiler.addInstruction(new BRA(startLabel));

        compiler.addLabel(endLabel);
        compiler.addInstruction(new LOAD_REG(GPRegister.H, saveReg));
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

}
