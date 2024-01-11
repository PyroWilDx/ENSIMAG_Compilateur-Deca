package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorManager;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.BOV;
import fr.ensimag.ima.pseudocode.instructions.REM;

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
    protected String getOperatorName() {
        return "%";
    }

}
