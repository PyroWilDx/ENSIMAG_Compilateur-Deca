package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

public class DeclMethod extends AbstractDeclMethod {
    private final AbstractIdentifier type;
    private final AbstractIdentifier name;
    private final ListParam params;
    // TODO peutetre creer une classe bloc pour factoriser avec Main et se rapprocher du sujet
    // TODO mais dans ce cas penser à changer le parser
    private final ListDeclVar listDeclVar;
    private final ListInst listInst;
    private String className;
    private boolean shouldRedeclare;
    private Label mStartLabel;
    private Label mEndLabel;

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name,
                      ListParam params, ListDeclVar listDeclVar, ListInst listInst) {
        this.type = type;
        this.name = name;
        this.params = params;
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
        this.className = null;
        this.shouldRedeclare = true;
        this.mStartLabel = null;
        this.mEndLabel = null;
    }

    public EnvironmentExp getEnvOfClass(DecacCompiler compiler, SymbolTable.Symbol classSymbol) {
        String errMsg = "Error in getEnvOfClass() of DeclMethod";

        TypeDefinition classTypeDef = compiler.environmentType.get(classSymbol);
        ClassDefinition classDef;
        try {
            classDef = classTypeDef.asClassDefinition(errMsg, getLocation());
        } catch (ContextualError e) {
            throw new UnsupportedOperationException(errMsg);
        }

        return classDef.getMembers();
    }

//    /**
//     * get the signature of this method.
//     *
//     * @param compiler compiler.
//     * @param vTable   vTable of the current class.
//     * @return if the method is in the class env, returns the signature of the method,
//     * else, return null.
//     */
//    private Signature getSignature(DecacCompiler compiler, VTable vTable) {
//        SymbolTable.Symbol methodSymbol = name.getName();
//
//        String errMsg = "Error in getSignature() of DeclMethod";
//
//        EnvironmentExp classEnv = getEnvOfClass(compiler, vTable.getClassSymbol());
//
//        MethodDefinition methodDef;
//        try {
//            methodDef = classEnv.get(methodSymbol).asMethodDefinition(errMsg, getLocation());
//        } catch (ContextualError e) {
//            return null;
//        }
//
//        return methodDef.getSignature();
//    }
//
//    /**
//     * search for the first definition of this method in superclasses, there is
//     * necessarily one.
//     *
//     * @param compiler compiler.
//     * @param vTable   vTable of the superclass.
//     * @return returns the symbol of the superclass.
//     */
//    private SymbolTable.Symbol searchForMethodInSuperClass(DecacCompiler compiler,
//                                                           VTable vTable, Signature methodSig) {
//        String errMsg = "Error in searchForMethodInSuperClass() of DeclMethod";
//        VTableManager vTM = compiler.getVTableManager();
//
//        VTable superClassVTable = vTM.getVTable(vTable.getSuperClassSymbol().getName());
//
//        SymbolTable.Symbol classSymbol = vTable.getClassSymbol();
//        SymbolTable.Symbol methodSymbol = name.getName();
//
//        EnvironmentExp superClassEnv = getEnvOfClass(compiler, classSymbol);
//
//        MethodDefinition superClassMethodDef;
//        try {
//            superClassMethodDef = superClassEnv.get(methodSymbol).asMethodDefinition(errMsg, getLocation());
//        } catch (ContextualError e) {
//            return searchForMethodInSuperClass(compiler, superClassVTable, methodSig);
//        }
//
//        if (methodSig.equals(superClassMethodDef.getSignature())) {
//            return vTable.getClassSymbol();
//        }
//
//        return searchForMethodInSuperClass(compiler, superClassVTable, methodSig);
//    }

//    /**
//     * @param compiler  compiler.
//     * @param vTable    vTable of the method's class.
//     * @param methodSig signature of the method.
//     * @return if the method is overloaded:
//     * returns the class symbol
//     * if the method is not overloaded, but defined in one of its superclasses:
//     * returns the symbol of the superclass
//     * if the method is not overloaded, and not defined in one of its superclasses:
//     * returns the class symbol
//     */
//    private SymbolTable.Symbol isMethodOverloaded(DecacCompiler compiler, VTable vTable,
//                                                  Signature methodSig, SymbolTable.Symbol classSymbol) {
//        String errMsg = "Error in isMethodOverloaded() of DeclMethod";
//        SymbolTable.Symbol superClassSymbol = vTable.getSuperClassSymbol();
//
//        if (superClassSymbol == null) {
//            return classSymbol;
//        }
//
//        VTableManager vTM = compiler.getVTableManager();
//
//        VTable superClassVTable = vTM.getVTable(superClassSymbol.getName());
//        SymbolTable.Symbol methodSymbol = name.getName();
//
//        EnvironmentExp superClassEnv = getEnvOfClass(compiler, superClassSymbol);
//
//        MethodDefinition superClassMethodDef;
//        try {
//            superClassMethodDef = superClassEnv.get(methodSymbol).asMethodDefinition(errMsg, getLocation());
//        } catch (ContextualError e) {
//            return isMethodOverloaded(compiler, superClassVTable, methodSig);
//        }
//
//        if (methodSig.equals(superClassMethodDef.getSignature())) {
//            return classSymbol;
//        }
//
//        return isMethodOverloaded(compiler, superClassVTable, methodSig);
//    }

    @Override
    public void codeGenVTable(DecacCompiler compiler, VTable vTable) {
        StackManager sM = compiler.getStackManager();

        className = vTable.getClassName();
        String methodName = name.getName().getName();

        mStartLabel = LabelUtils.getMethodLabel(className, methodName);
        mEndLabel = LabelUtils.getMethodEndLabel(className, methodName);
        compiler.addInstruction(new LOAD(new LabelOperand(mStartLabel), Register.R0));

        DAddr mAddr = sM.getGbOffsetAddr();
        compiler.addInstruction(new STORE(Register.R0, mAddr));
        sM.incrVTableCpt();

        vTable.addMethod(methodName, mAddr);

        String paramName;
        int currParamOffset = -3;
        for (AbstractParam param : params.getList()) {
            paramName = param.getName().getName();
            vTable.addParamToMethod(methodName, paramName, currParamOffset);
            currParamOffset++;
        }
        // Done
    }

    @Override
    public void codeGenDeclMethod(DecacCompiler compiler) {
        if (!shouldRedeclare) return;

        RegManager rM = compiler.getRegManager();
        StackManager sM = new StackManager();
        compiler.setStackManager(sM);

        String methodName = name.getName().getName();

        compiler.addLabel(mStartLabel);
        int iTSTO = compiler.getProgramLineCount();

        rM.saveUsedRegs();
        rM.freeAllRegs();
        // TODO (inverser la liste des regs, comme ca on a moins de réutilisation ???)

        listInst.codeGenListInst(compiler);

        RegManager.RegStatus[] usedRegs = rM.popUsedRegs();
        RegManager.addSaveRegsInsts(compiler, iTSTO, usedRegs);

        compiler.addInstruction(new WSTR("Error: Exiting function " + className +
                "." + methodName + " without return"));
        compiler.addInstruction(new WNL());
        compiler.addInstruction(new ERROR());

        compiler.addLabel(mEndLabel);
        RegManager.addRestoreRegsInsts(compiler, usedRegs);

        compiler.addInstruction(new RTS());
        // Done
    }

//    // TODO (à déplacer)
//    public void codeGenInstanceOf(DecacCompiler compiler) {
//        RegManager rM = compiler.getRegManager();
//        CondManager cM = compiler.getCondManager();
//        VTableManager vTM = compiler.getVTableManager();
//
//        compiler.addInstruction(
//                new LOAD(vTM.getAddrOfClass(targetClassName), Register.R0));
//        GPRegister gpReg = rM.getFreeReg();
//        compiler.addInstruction(
//                new LOAD(identifier.getExpDefinition().getOperand(), gpReg));
//        int idCpt = cM.getAndIncrIdCpt();
//        Label startLabel = new Label("startInstanceOf" + idCpt);
//        Label endTrueLabel = new Label("endTrueInstanceOf" + idCpt);
//        Label endFalseLabel = new Label("endFalseInstanceOf" + idCpt);
//        Label endLabel = new Label("endInstanceOf" + idCpt);
//
//        compiler.addLabel(startLabel);
//
//        compiler.addInstruction(
//                new LOAD(new RegisterOffset(0, gpReg), gpReg));
//
//        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
//        compiler.addInstruction(new BEQ(endFalseLabel));
//
//        compiler.addInstruction(new CMP(Register.R0, gpReg));
//        compiler.addInstruction(new BEQ(endTrueLabel));
//
//        compiler.addInstruction(new BRA(startLabel));
//
//        compiler.addLabel(endTrueLabel);
//        compiler.addInstruction(new LOAD(1, gpReg));
//        compiler.addInstruction(new BRA(endLabel));
//
//        compiler.addLabel(endFalseLabel);
//        compiler.addInstruction(new LOAD(0, gpReg));
//
//        compiler.addLabel(endLabel);
//        rM.freeReg(gpReg);
//    }

    @Override
    public SymbolTable.Symbol getName() {
        return null;
    }

    @Override
    public EnvironmentExp verifyDeclMethodMembers(DecacCompiler compiler, SymbolTable.Symbol superClass, int index) throws ContextualError {
        Type t = this.type.verifyType(compiler);
        Signature sig = this.params.verifyListDeclParamMembers(compiler);
        TypeDefinition def = compiler.environmentType.get(superClass);
        if (def.isClass()) {
            ClassDefinition superClassDef = (ClassDefinition) def;
            EnvironmentExp envExpSuper = superClassDef.getMembers();
            ExpDefinition expDef = envExpSuper.get(this.name.getName());
            if (expDef != null) {
                if (!expDef.isMethod()) {
                    throw new ContextualError("A field '" +
                            this.name.getName() + "' already " +
                            "exists in super class", getLocation());
                }
                MethodDefinition methodDefinition = (MethodDefinition) expDef;
                Signature sig2 = methodDefinition.getSignature();
                if (!sig.equals(sig2)) {
                    throw new ContextualError("Method defined in super class with" +
                            "another signature.", getLocation());
                }
                Type type2 = expDef.getType();
                if (!compiler.environmentType.subtype(t, type2)) {
                    throw new ContextualError("Return type of override must be" +
                            " subtype of the return type of the method declared in super class", getLocation());
                }
            }
        }
        EnvironmentExp env = new EnvironmentExp(null);
        ExpDefinition newDef = new MethodDefinition(t, getLocation(), sig, index);
        try {
            env.declare(this.name.getName(), newDef);
        } catch (EnvironmentExp.DoubleDefException e) {
            throw new DecacInternalError("Symbol cannot have been declared twice.");
        }
        return env;
        // Done
    }

    @Override
    public void verifyDeclMethodBody(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type returnType = this.type.verifyType(compiler);
        EnvironmentExp envParams = this.params.verifyListDeclParamBody(compiler);
        EnvironmentExp envReturn = this.listDeclVar.verifyListDeclVariable(compiler, localEnv, envParams, currentClass);
        EnvironmentExp envEmpile = EnvironmentExp.empile(envReturn, localEnv);
        this.listInst.verifyListInst(compiler, envEmpile, currentClass, returnType);
        // Done
    }

    @Override
    public void decompile(IndentPrintStream s) {
        type.decompile(s);
        s.print(" ");
        name.decompile(s);
        s.print("(");
        params.decompile(s);
        s.println(") {");
        s.indent();
        listDeclVar.decompile(s);
        listInst.decompile(s);
        s.unindent();
        s.println("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        // TODO
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);

    }

    @Override
    protected void iterChildren(TreeFunction f) {
        // TODO
        throw new DecacInternalError("not implemented yet");
    }
}
