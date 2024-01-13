package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorManager;
import fr.ensimag.deca.codegen.LabelUtils;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr {
    private AbstractIdentifier type;

    public New(AbstractIdentifier type) {
        this.type = type;
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        if (!t.isClass()) {
            throw new ContextualError("'" + t.getName() +
                    "' is not a class.", getLocation());
        }
        this.setType(t);
        return t;
        // Done
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        ErrorManager eM = compiler.getErrorManager();
        VTableManager vTM = compiler.getVTableManager();

        String className = getType().getName().getName();
        vTM.setCurrClassName(className);

        int fieldsCount = vTM.getCurrFieldCountOfClass();

        GPRegister gpReg = rM.getFreeReg();
        compiler.addInstruction(new NEW(fieldsCount + 1, gpReg));
        compiler.addInstruction(new BOV(eM.getHeapOverflowLabel()));
        compiler.addInstruction(new LEA(vTM.getCurrAddrOfClass(), Register.R0));
        compiler.addInstruction(
                new STORE(Register.R0, new RegisterOffset(0, gpReg)));
        compiler.addInstruction(new PUSH(gpReg));
        rM.freeReg(gpReg);
        compiler.addInstruction(new BSR(LabelUtils.getClassInitLabel(vTM.getCurrClassName())));
        compiler.addInstruction(new POP(gpReg));
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        type.decompile(s);
        s.println("();");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iterChildren(f);
    }
}
