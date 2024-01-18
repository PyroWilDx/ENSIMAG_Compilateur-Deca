package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.*;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;

public class FieldSelection extends AbstractLValue {
    private final AbstractExpr expr;
    private final AbstractIdentifier fieldIdent;

    public FieldSelection(AbstractExpr expr, AbstractIdentifier fieldIdent) {
        this.expr = expr;
        this.fieldIdent = fieldIdent;
    }

    @Override
    public Type verifyLValue(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {

        Type expressiontype = this.expr.verifyExpr(compiler, localEnv, currentClass);
        TypeDefinition exprTypeDef = compiler.environmentType.get(expressiontype.getName());
        ClassDefinition exprClassDef = exprTypeDef.asClassDefinition("Expression type must be a class.", getLocation());

        if (!expressiontype.isClass()) {
            throw new DecacInternalError("Expression has a non-class type but Environment contains a class " +
                    "definition for that type.");
        }

        EnvironmentExp exprClassEnv = exprClassDef.getMembers();

        FieldIdentNonTerminalReturn fieldIdentResult = this.fieldIdent.verifyFieldIdent(exprClassEnv);
        Visibility visib = fieldIdentResult.getVisibility();
        ClassDefinition fieldContainingClass = fieldIdentResult.getContainingClass();
        Type type = fieldIdentResult.getType();

        // Conditions
        if (visib == Visibility.PROTECTED) {
            // la classe de l'expression doit être sous classe de la classe courante.
            // la classe courante doit être sous classe de la classe contenant la méthode.
            if (!(compiler.environmentType.subtype(expressiontype, currentClass.getType()) &&
                    compiler.environmentType.subtype(currentClass.getType(), fieldContainingClass.getType()))) {
                throw new ContextualError("'" + this.fieldIdent.getName() +
                        "' is protected.", getLocation());
            }
        }
        this.setType(type);
        return type;
        // Done
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        CondManager cM = compiler.getCondManager();

        if (cM.isDoingCond()) {
            fieldIdent.isInTrue = isInTrue;
            fieldIdent.branchLabel = branchLabel;
        }

        DAddr fAddr = getAddrOfField(compiler);
        fieldIdent.getExpDefinition().setOperand(fAddr);

        fieldIdent.codeGenInst(compiler);

        fieldIdent.getExpDefinition().setOperand(null);
        // Done
    }

    @Override
    protected void codeGenInstGb(DecacCompiler compiler) {
        // TODO (GB)
        CondManager cM = compiler.getCondManager();
        GameBoyManager gbM = compiler.getGameBoyManager();

        if (cM.isDoingCond()) {
            fieldIdent.isInTrue = isInTrue;
            fieldIdent.branchLabel = branchLabel;
        }

        DAddr fAddr = getAddrOfFieldGb(compiler);
        fieldIdent.getExpDefinition().setOperand(fAddr);

        if (expr instanceof AbstractIdentifier) {
            gbM.currClassVarStack.addFirst((AbstractIdentifier) expr);
        }

        fieldIdent.codeGenInst(compiler);

        fieldIdent.getExpDefinition().setOperand(null);

        if (expr instanceof AbstractIdentifier) {
            gbM.currClassVarStack.removeFirst();
        }
        // Done
    }

    public DAddr getAddrOfField(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        ErrorManager eM = compiler.getErrorManager();
        VTableManager vTM = compiler.getVTableManager();

        vTM.enterClass(expr.getType().getName().getName());

        String fieldName = fieldIdent.getName().getName();

        expr.codeGenInst(compiler);

        GPRegister gpReg = rM.getLastReg();
        if (!(expr instanceof This)) {
            if (compiler.getCompilerOptions().doCheck()) {
                compiler.addInstruction(new CMP(new NullOperand(), gpReg));
                compiler.addInstruction(new BEQ(eM.getNullPointerLabel()));
            }
        } // Else, pas besoin vu qu'on est déjà dans une instance de la classe

        int fieldOffset = vTM.getCurrFieldOffset(fieldName);
        DAddr fAddr = new RegisterOffset(fieldOffset, gpReg);
        rM.freeReg(gpReg);

        vTM.exitClass();

        return fAddr;
    }

    public DAddr getAddrOfFieldGb(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        ErrorManager eM = compiler.getErrorManager();
        VTableManager vTM = compiler.getVTableManager();

        vTM.enterClass(expr.getType().getName().getName());

        String fieldName = fieldIdent.getName().getName();

        expr.codeGenInst(compiler);

        GPRegister gpReg = rM.getLastReg();
        if (!(expr instanceof This)) {
            if (compiler.getCompilerOptions().doCheck()) {
                compiler.addInstruction(new CMP(new NullOperand(), gpReg));
                compiler.addInstruction(new BEQ(eM.getNullPointerLabel()));
            }
        } // Else, pas besoin vu qu'on est déjà dans une instance de la classe

        int fieldOffset = vTM.getCurrFieldOffset(fieldName);
        DAddr fAddr = new RegisterOffset(fieldOffset, gpReg);
        rM.freeReg(gpReg);

        vTM.exitClass();

        return fAddr;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        expr.decompile(s);
        s.print(".");
        fieldIdent.decompile(s);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        expr.prettyPrint(s, prefix, false);
        fieldIdent.prettyPrint(s, prefix, true);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        expr.iter(f);
        fieldIdent.iter(f);
    }
}
