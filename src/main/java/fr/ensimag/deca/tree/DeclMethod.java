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

    public boolean isOverride() {
        return this.override;
    }

    @Override
    public int getMethodIndex() {
        return methodIndex;
    }

    public DeclMethod(AbstractIdentifier type, AbstractIdentifier name,
                      ListDeclParam params, ListDeclVar listDeclVar, ListInst listInst) {
        this.type = type;
        this.name = name;
        this.params = params;
        this.listDeclVar = listDeclVar;
        this.listInst = listInst;
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
    public SymbolTable.Symbol getName() {
        return this.name.getName();
    }

    @Override
    public AbstractIdentifier getTypeIdent() {
        return this.type;
    }

    @Override
    public AbstractIdentifier getNameIdent() {
        return this.name;
    }

    @Override
    public ListDeclParam getParams() {
        return this.params;
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
            if (compiler.getCompilerOptions().doCheck()) {
                compiler.addInstruction(new WSTR("Error: Exiting function " + className +
                        "." + methodName + "() without return"));
                compiler.addInstruction(new WNL());
                compiler.addInstruction(new ERROR());
            }
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
        s.println("");
        s.unindent();
        s.println("}");
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
