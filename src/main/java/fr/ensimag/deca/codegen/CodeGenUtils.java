package fr.ensimag.deca.codegen;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.tree.AbstractIdentifier;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.LOAD;

import java.util.LinkedList;

public class CodeGenUtils {

    private CodeGenUtils() {

    }

    public static DAddr extractAddrFromIdent(DecacCompiler compiler, AbstractIdentifier ident) {
        RegManager rM = compiler.getRegManager();
        VTableManager vTM = compiler.getVTableManager();

        DAddr iAddr = ident.getExpDefinition().getOperand();
        if (iAddr == null) { // It's a Method Param or Class Field
            String identName = ident.getName().getName();
            Integer paramOffset = vTM.getCurrParamOffsetOfMethod(identName);
            if (paramOffset != null) { // It's a Method Param
                iAddr = new RegisterOffset(paramOffset, Register.LB);
            } else { // It's a Class Field
                GPRegister gpReg = rM.getFreeReg();
                compiler.addInstruction(
                        new LOAD(new RegisterOffset(-2, Register.LB), gpReg));
                // Pas besoin vu qu'on est déjà dans une instance de la classe
//                compiler.addInstruction(new CMP(new NullOperand(), Register.R0));
//                compiler.addInstruction(new BEQ(eM.getNullPointerLabel()));
                Integer fieldOffset = null;
                LinkedList<String> popeds = new LinkedList<>();
                while (fieldOffset == null) {
                    fieldOffset = vTM.getCurrFieldOffset(identName);
                    if (fieldOffset != null) {
                        iAddr = new RegisterOffset(fieldOffset, gpReg);
                    }
                    if (fieldOffset == null) {
                        popeds.addFirst(vTM.getCurrClassName());
                        vTM.exitClass();
                    }
                    if (vTM.getCurrClassName() == null) {
                        iAddr = null;
                        break;
                    }
                }
                for (String poped : popeds) {
                    vTM.enterClass(poped);
                }
                rM.freeReg(gpReg);
            }
        }

        return iAddr;
    }

}
