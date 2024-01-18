package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.ErrorManager;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Label;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Modulo extends AbstractOpArith {

    public Modulo(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenOpArith(DecacCompiler compiler,
                                  DVal valReg, GPRegister saveReg) {
        ErrorManager eM = compiler.getErrorManager();

        compiler.addInstruction(new REM(valReg, saveReg));
        if (compiler.getCompilerOptions().doCheck()) {
            compiler.addInstruction(new BOV(eM.getDivBy0Label()));
        }
        // Done
    }

    @Override
    protected void codeGenOpArithGb(DecacCompiler compiler,
                                    DVal valReg, GPRegister saveReg) {
        CondManager cM = compiler.getCondManager();

        Label startLabel = new Label("ModuloStart" + cM.getUniqueId());
        Label endLabel = new Label("ModuloEnd" + cM.getUniqueId());

        compiler.addInstruction(new LOAD_GEN(valReg, Register.A));
        compiler.addInstruction(new CMP_A(0, Register.A));
        compiler.addInstruction(new BEQ(endLabel));

        compiler.addInstruction(new LOAD_REG(saveReg, Register.A));

        compiler.addLabel(startLabel);
        compiler.addInstruction(new CMP_A(valReg, Register.A));
        compiler.addInstruction(new BLT(endLabel));
        compiler.addInstruction(new SUB_A(valReg, Register.A));

        compiler.addInstruction(new BRA(startLabel));
        compiler.addLabel(endLabel);
        compiler.addInstruction(new LOAD_REG(Register.A, saveReg));
    }

    @Override
    protected String getOperatorName() {
        return "%";
    }

}
