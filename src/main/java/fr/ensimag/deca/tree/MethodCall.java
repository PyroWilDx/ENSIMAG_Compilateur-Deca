package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
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
    private AbstractExpr expr;
    private AbstractIdentifier methodIdent;
    private RValueStar rValueStar;

    @Override
    public Type verifyExpr(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
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
        this.rValueStar.verifyRValueStar(compiler, localEnv, currentClass, sig);
        return t;
        // Done
    }

    @Override
    public void codeGenMethodCall(DecacCompiler compiler) {
//        RegManager rM = compiler.getRegManager();
//        VTableManager vTM = compiler.getVTableManager();
//
//        int nbParam = params.size() + 1;
//        compiler.addInstruction(new ADDSP(nbParam));
//
//        GPRegister gpReg = rM.getFreeReg();
//        compiler.addInstruction(
//                new LOAD(methodIdent.getExpDefinition().getOperand(), gpReg));
//
//        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
//        compiler.addInstruction(new BEQ(ErrorUtils.nullPointerLabel));
//
//        compiler.addInstruction(
//                new STORE(gpReg, new RegisterOffset(0, Register.SP)));
//        rM.freeReg(gpReg);
//
//        int currParamIndex = -1;
//        for (AbstractInst inst : insts.getList()) {
//            inst.codeGenInst(compiler);
//            gpReg = rM.getLastReg();
//            compiler.addInstruction(
//                    new STORE(gpReg, new RegisterOffset(currParamIndex, Register.SP)));
//            rM.freeReg(gpReg);
//            currParamIndex--;
//        }
//
//        gpReg = rM.getFreeReg();
////        compiler.addInstruction(
////                new LOAD(new RegisterOffset(0, Register.SP), gpReg));
////        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
////        compiler.addInstruction(new BEQ(ErrorUtils.nullPointerLabel));
//
////        compiler.addInstruction(
////                new LOAD(new RegisterOffset(0, gpReg), gpReg));
////        compiler.addInstruction(new BSR(new RegisterOffset(mOffset, gpReg)));
//        compiler.addInstruction(vTM.getAddrOfMethod(className, methodName));
//        rM.freeReg(gpReg);
//
//        compiler.addInstruction(new SUBSP(nbParam));
    }

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
