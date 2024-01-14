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
    private final ListDeclParam params;
    // TODO peutetre creer une classe bloc pour factoriser avec Main et se rapprocher du sujet
    // TODO mais dans ce cas penser à changer le parser
    private final ListDeclVar listDeclVar;
    private final ListInst listInst;
    private String className;
    private Label mStartLabel;
    private Label mEndLabel;
    private boolean override = false;
    private int methodIndex;
    public boolean isOverride() {
        return this.override;
    }

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name,
                      ListDeclParam params, ListDeclVar listDeclVar, ListInst listInst) {
        this.type = type;
        this.name = name;
        this.params = params;
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
        this.className = null;
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

    @Override
    public void codeGenVTable(DecacCompiler compiler, VTable vTable) {
        StackManager sM = compiler.getStackManager();

        className = vTable.getClassName();
        String methodName = name.getName().getName();

        mStartLabel = LabelUtils.getMethodLabel(className, methodName);
        mEndLabel = LabelUtils.getMethodEndLabel(className, methodName);
        compiler.addInstruction(new LOAD(new LabelOperand(mStartLabel), Register.R0));

        DAddr mAddr = sM.getOffsetAddr();
        compiler.addInstruction(new STORE(Register.R0, mAddr));
        sM.incrVTableCpt();

        vTable.addMethod(methodName, mAddr);

        String paramName;
        int currParamOffset = -3;
        for (AbstractParam param : params.getList()) {
            paramName = param.getName().getName();
            vTable.addParamToMethod(methodName, paramName, currParamOffset);
            currParamOffset--;
        }
        // Done
    }

    @Override
    public void codeGenDeclMethod(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        StackManager sM = new StackManager(true);
        compiler.setStackManager(sM);
        VTableManager vTM = compiler.getVTableManager();

        String methodName = name.getName().getName();
        vTM.enterMethod(methodName);

        compiler.addLabel(mStartLabel);
        int iTSTO = compiler.getProgramLineCount();

        rM.saveUsedRegs();
        rM.freeAllRegs();

        listDeclVar.codeGenListDeclVar(compiler);

        listInst.codeGenListInst(compiler);

        boolean[] usedRegs = rM.popUsedRegs();
        RegManager.addSaveRegsInsts(compiler, iTSTO, usedRegs);

        if (!type.getType().isVoid()) {
            compiler.addInstruction(new WSTR("Error: Exiting function " + className +
                    "." + methodName + "() without return"));
            compiler.addInstruction(new WNL());
            compiler.addInstruction(new ERROR());
        }

        compiler.addLabel(mEndLabel);
        RegManager.addRestoreRegsInsts(compiler, usedRegs);

        compiler.addInstruction(new RTS());

        vTM.exitMethod();
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
        return this.name.getName();
    }

    @Override
    public EnvironmentExp verifyDeclMethodMembers(DecacCompiler compiler, SymbolTable.Symbol superClass, int index) throws ContextualError {
        int realIndex = index;
        Type t = this.type.verifyType(compiler);
        Signature sig = this.params.verifyListDeclParamMembers(compiler);
        TypeDefinition def = compiler.environmentType.get(superClass);
        if (def.isClass()) {
            ClassDefinition superClassDef = (ClassDefinition) def;
            EnvironmentExp envExpSuper = superClassDef.getMembers();
            ExpDefinition expDef = envExpSuper.get(this.name.getName());
            if (expDef != null) {
                this.override = true;
                if (!expDef.isMethod()) {
                    throw new ContextualError("A field '" +
                            this.name.getName() + "' already " +
                            "exists in super class.", getLocation());
                }
                MethodDefinition methodDefinition = (MethodDefinition) expDef;
                Signature sig2 = methodDefinition.getSignature();
                if (!sig.equals(sig2)) {
                    throw new ContextualError("Method '"+ this.getName() +
                            "' defined in super class with " +
                            "another signature.", getLocation());
                }
                Type type2 = expDef.getType();
                if (!compiler.environmentType.subtype(t, type2)) {
                    throw new ContextualError("Return type of override must be" +
                            " subtype of the return type of the method declared in super class.", getLocation());
                }
                realIndex = methodDefinition.getIndex();
            }
        }
        EnvironmentExp env = new EnvironmentExp(null);
        ExpDefinition newDef = new MethodDefinition(t, getLocation(), sig, realIndex);
        this.methodIndex = realIndex;
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
        s.print(") {");
        s.indent();
        listDeclVar.decompile(s);
        listInst.decompile(s);
        s.unindent();
        s.print("}");
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        s.println(prefix + "index : " + this.methodIndex);
        type.prettyPrint(s, prefix, false);
        name.prettyPrint(s, prefix, false);
        params.prettyPrint(s, prefix, false);
        listDeclVar.prettyPrint(s, prefix, false);
        listInst.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iterChildren(f);
        name.iterChildren(f);
        params.iterChildren(f);
        listDeclVar.iterChildren(f);
        listInst.iterChildren(f);
    }
}
