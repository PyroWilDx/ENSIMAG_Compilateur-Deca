package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.deca.tree.Identifier;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

public class Utils {

    private Utils() {

    }

    public static DAddr extractAddrFromIdent(DecacCompiler compiler, AbstractIdentifier ident) {
        ErrorManager eM = compiler.getErrorManager();
        VTableManager vTM = compiler.getVTableManager();

        DAddr iAddr = ident.getExpDefinition().getOperand();
        if (iAddr == null) { // It's a Method Param or Class Field
            String identName = ident.getName().getName();
            Integer paramOffset = vTM.getCurrParamOffsetOfMethod(identName);
            if (paramOffset != null) { // It's a Method Param
                iAddr = new RegisterOffset(paramOffset, Register.LB);
            } else { // It's a Class Field
                compiler.addInstruction(
                        new LOAD(new RegisterOffset(-2, Register.LB), Register.R0));
                compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
                compiler.addInstruction(new BEQ(eM.getNullPointerLabel()));
                int fieldOffset = vTM.getCurrOffsetOfField(identName);
                iAddr = new RegisterOffset(fieldOffset, Register.R0);
            }
        }

        return iAddr;
    }

}
