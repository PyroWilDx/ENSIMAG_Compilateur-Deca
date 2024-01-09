package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.LabelOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LOAD;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.util.LinkedList;
import java.util.List;

public class ListDeclMethod extends TreeList<AbstractDeclMethod> {
    @Override
    public void decompile(IndentPrintStream s) {
        for (AbstractDeclMethod decl : getList()) {
            decl.decompile(s);
        }
    }

    public EnvironmentExp verifyListDeclMethodMembers(DecacCompiler compiler,
                                                      SymbolTable.Symbol superClass) throws ContextualError {
        EnvironmentExp envReturn = new EnvironmentExp(null);
        int index = 1;
        for (AbstractDeclMethod decl : this.getList()) {
            EnvironmentExp env = decl.verifyDeclMethodMembers(compiler, superClass, index);
            index++;
            if (envReturn.disjointUnion(env) != null) {
                throw new ContextualError("Method '" + decl.getName()
                        + "' already defined.", getLocation());
            }
        }
        return envReturn;
    }

    public void verifyListDeclMethodBody(DecacCompiler compiler,
                                         EnvironmentExp localEnv,
                                         ClassDefinition currentClass) throws ContextualError {
        for (AbstractDeclMethod decl : getList()) {
            decl.verifyDeclMethodBody(compiler, localEnv, currentClass);
        }
    }

    public void codeGenVTable(DecacCompiler compiler, VTable vTable) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        VTable superClassVTable = vTable.getVTableOfSuperClass(vTM);
        LinkedList<MethodInfo> superClassMethods =
                superClassVTable.getClassMethodsOrderered();
        for (MethodInfo methodInfo : superClassMethods) {
            boolean isPresentInCurrClass = false;
            for (AbstractDeclMethod declMethod : getList()) {
                if (methodInfo.getMethodName().equals(declMethod.getName().getName())) {
                    isPresentInCurrClass = true;
                }
            }
            if (!isPresentInCurrClass) {
                compiler.addInstruction(new LOAD(new LabelOperand(
                        LabelUtils.getMethodLabel(methodInfo.getClassName(),
                                methodInfo.getMethodName())),
                        Register.R0));
                DAddr mAddr = sM.getGbOffsetAddr();
                compiler.addInstruction(new STORE(Register.R0, mAddr));
                sM.incrVTableCpt();

                vTable.addSuperMethod(methodInfo.getClassName(),
                        methodInfo.getMethodName(), mAddr);
                vTable.copyMethodParams(superClassVTable, methodInfo.getMethodName());
            }
        }

        for (AbstractDeclMethod declMethod : getList()) {
            declMethod.codeGenVTable(compiler, vTable);
        }
        // Done
    }

    public void codeGenListDeclMethod(DecacCompiler compiler) {
        for (AbstractDeclMethod method : getList()) {
            method.codeGenDeclMethod(compiler);
        }
    }

}
