package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.ClassDefinition;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.EnvironmentExp;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class New extends AbstractExpr {
    private final AbstractIdentifier type;

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

        vTM.enterClass(type.getType().getName().getName());

        int fieldsCount = vTM.getCurrFieldCountOfClass();

        GPRegister gpReg = rM.getFreeReg();

        compiler.addInstruction(new NEW(fieldsCount + 1, gpReg));
        if (compiler.getCompilerOptions().doCheck()) {
            compiler.addInstruction(new BOV(eM.getHeapOverflowLabel()));
        }
        compiler.addInstruction(new LEA(vTM.getCurrClassAddr(), Register.R0));
        compiler.addInstruction(
                new STORE(Register.R0, new RegisterOffset(0, gpReg)));
        compiler.addInstruction(new PUSH(gpReg));
        compiler.addInstruction(new BSR(LabelUtils.getClassInitLabel(vTM.getCurrClassName())));
        compiler.addInstruction(new POP(gpReg));

        rM.freeReg(gpReg);

        vTM.exitClass();
        // Done
    }

    @Override
    protected void codeGenInstGb(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        VTableManager vTM = compiler.getVTableManager();
        GameBoyManager gbM = compiler.getGameBoyManager();

        vTM.enterClass(type.getType().getName().getName());

        int fieldsCount = vTM.getCurrFieldCountOfClass();
        for (int i = 0; i < fieldsCount; i++) {
            gbM.addFieldVar();
        }
        gbM.setCurrNewFieldCount(fieldsCount);

        GPRegister gpReg = rM.getFreeReg();

        compiler.addInstruction(new LOAD_SP(Register.SP, Register.HL, -1));
        compiler.addInstruction(new SUBSP(fieldsCount * 2 + 2)); // WTF ??
        compiler.addInstruction(new PUSH(Register.HL));
        compiler.addInstruction(new BSR(LabelUtils.getClassInitLabel(vTM.getCurrClassName())));
        compiler.addInstruction(new POP(gpReg));
        compiler.addInstruction(new ADDSP(fieldsCount * 2 + 2)); // WTF ??

        rM.freeReg(gpReg);

        vTM.exitClass();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("new ");
        type.decompile(s);
        s.print("()");
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
