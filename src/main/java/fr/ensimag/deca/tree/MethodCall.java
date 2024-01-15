package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorManager;
import fr.ensimag.deca.codegen.RegManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.NullOperand;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class MethodCall extends AbstractMethodCall {
    private final AbstractExpr expr;
    private final AbstractIdentifier methodIdent;
    private final RValueStar rValueStar;

    public MethodCall(AbstractExpr expr,AbstractIdentifier methodIdent,RValueStar rValueStar){
        this.expr = expr;
        this.methodIdent = methodIdent;
        this.rValueStar = rValueStar;
    }
    public Type verifyMethodCall(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type type = this.expr.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition definition = compiler.environmentType.get(type.getName());
        ClassDefinition classDefinition = definition.asClassDefinition("Method call on native type.", getLocation());

        if (!type.isClass()) {
            throw new DecacInternalError("Inconsistent type, class or not class??");
        }
        EnvironmentExp classEnv = classDefinition.getMembers();
        MethodIdentNonTerminalReturn sigAndType = this.methodIdent.verifyMethodIdent(classEnv);
        Signature sig = sigAndType.getSignature();
        Type t = sigAndType.getType();
        this.setType(t);
        this.rValueStar.verifyRValueStar(compiler, localEnv, currentClass, sig);
        return t;
        // Done
    }

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        return this.verifyMethodCall(compiler, localEnv, currentClass);
    }

    @Override
    public void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        ErrorManager eM = compiler.getErrorManager();
        VTableManager vTM = compiler.getVTableManager();

        vTM.enterClass(expr.getType().getName().getName());

        String methodName = methodIdent.getName().getName();
        vTM.enterMethod(methodName);

        int nbParam = vTM.getCurrParamCountOfMethod() + 1;
        compiler.addInstruction(new ADDSP(nbParam));

        expr.codeGenInst(compiler);

        GPRegister gpReg = rM.getLastReg();
        compiler.addInstruction(new STORE(gpReg, new RegisterOffset(0, Register.SP)));
        rM.freeReg(gpReg);

        int currParamIndex = -1;
        for (AbstractExpr arg : rValueStar.getList()) {
            arg.codeGenInst(compiler);
            gpReg = rM.getLastRegOrImm(compiler);
            compiler.addInstruction(
                    new STORE(gpReg, new RegisterOffset(currParamIndex, Register.SP)));
            rM.freeReg(gpReg);
            currParamIndex--;
        }

        gpReg = rM.getFreeReg();
        compiler.addInstruction(new LOAD(new RegisterOffset(0, Register.SP), gpReg));
        if (compiler.getCompilerOptions().doCheck()) {
            compiler.addInstruction(new CMP(new NullOperand(), gpReg));
            compiler.addInstruction(new BEQ(eM.getNullPointerLabel()));
        }
        compiler.addInstruction(new LOAD(new RegisterOffset(0, gpReg), gpReg));
        compiler.addInstruction(new BSR(new RegisterOffset(vTM.getCurrMethodOffset(), gpReg)));
        rM.freeReg(gpReg);

        compiler.addInstruction(new SUBSP(nbParam));

        if (!getType().isVoid()) {
            rM.freeRegForce(Register.R0);
        }

        vTM.exitMethod();
        vTM.exitClass();
    }

    @Override
    public void decompile(IndentPrintStream s) {
        this.expr.decompile(s);
        s.print(".");
        this.methodIdent.decompile(s);
        s.print("(");
        this.rValueStar.decompile(s);
        s.print(")");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        methodIdent.prettyPrint(s, prefix, false);
        rValueStar.prettyPrint(s,prefix,true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        methodIdent.iter(f);
        rValueStar.iter(f);
    }
}
