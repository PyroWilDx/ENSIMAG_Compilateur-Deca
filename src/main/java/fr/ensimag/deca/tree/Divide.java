package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.DIV;
import fr.ensimag.ima.pseudocode.instructions.QUO;

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
                                  GPRegister lReg, GPRegister rReg) {
        if (getLeftOperand().getType().isInt() && getRightOperand().getType().isInt()) {
            compiler.addInstruction(new QUO(lReg, rReg));
        } else {
            compiler.addInstruction(new DIV(lReg, rReg));
        }
        // TODO, faut-il convFloat ou c'est déjà fait dans l'étape B ?
    }

    @Override
    protected String getOperatorName() {
        return "/";
    }

}
