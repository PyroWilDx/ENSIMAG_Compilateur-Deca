package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.CondManager;
import fr.ensimag.deca.codegen.GameBoy;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class Multiply extends AbstractOpArith {
    public Multiply(AbstractExpr leftOperand, AbstractExpr rightOperand) {
        super(leftOperand, rightOperand);
    }

    @Override
    protected void codeGenOpArith(DecacCompiler compiler,
                                  DVal valReg, GPRegister saveReg) {
        CondManager cM = compiler.getCondManager();

        if (GameBoy.doCp) {
            if (!(valReg instanceof GPRegister)) {
                int value = 0;
                if (valReg instanceof ImmediateInteger) {
                    ImmediateInteger valImmInt = (ImmediateInteger) valReg;
                    value = valImmInt.getValue();
                } else if (valReg instanceof ImmediateFloat) {
                    ImmediateFloat valImmFloat = (ImmediateFloat) valReg;
                    value = valImmFloat.getIntValue();
                }
                for (int i = 0; i < value; i++) {
                    compiler.addInstruction(new ADD(saveReg, saveReg));
                }
            } else {
                Label startLabel = new Label("MulStart" + cM.getUniqueId());
                Label set0Label = new Label("Mul0" + cM.getUniqueId());
                Label endLabel = new Label("MulEnd" + cM.getUniqueId());

                GPRegister valGpReg = (GPRegister) valReg;

                compiler.addInstruction(new CMP(0, valGpReg));
                compiler.addInstruction(new BEQ(set0Label));

                compiler.addLabel(startLabel);
                compiler.addInstruction(new SUB(1, valGpReg));
                compiler.addInstruction(new CMP(0, valGpReg));
                compiler.addInstruction(new BEQ(endLabel));

                compiler.addInstruction(new ADD(saveReg, saveReg));

                compiler.addInstruction(new BRA(startLabel));

                compiler.addLabel(set0Label);
                compiler.addInstruction(new LOAD(0, saveReg));

                compiler.addLabel(endLabel);
            }
        } else {
            compiler.addInstruction(new MUL(valReg, saveReg));
        }
    }

    @Override
    protected String getOperatorName() {
        return "*";
    }

}
