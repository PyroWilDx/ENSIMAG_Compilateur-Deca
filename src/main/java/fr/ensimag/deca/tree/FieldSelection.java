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
import fr.ensimag.ima.pseudocode.RegisterOffset;
import fr.ensimag.ima.pseudocode.instructions.BEQ;
import fr.ensimag.ima.pseudocode.instructions.CMP;

import java.io.PrintStream;

public class FieldSelection extends AbstractLValue {
    private AbstractExpr expr;
    private AbstractIdentifier fieldIdent;

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
            if (!compiler.environmentType.subtype(expressiontype, currentClass.getType())) {
                throw new ContextualError("'" + this.fieldIdent.getName() +
                        "' is protected, hence '" + exprClassDef.getType().getName() +
                        "' must be a subtype of '" + currentClass.getType().getName() + "'.", getLocation());
            }
            // la classe courante doit être sous classe de la classe contenant la méthode.
            if (!compiler.environmentType.subtype(currentClass.getType(), fieldContainingClass.getType())) {
                throw new ContextualError("'" + this.fieldIdent.getName() +
                        "' is protected, hence '" + currentClass.getType().getName() +
                        "' must be a subtype of '" + fieldContainingClass.getType().getName() + "'.", getLocation());
            }
        }
        return type;
        // Done
    }

    @Override
    protected void codeGenInst(DecacCompiler compiler) {
        RegManager rM = compiler.getRegManager();
        VTableManager vTM = compiler.getVTableManager();

        String fieldName = fieldIdent.getName().getName();

        expr.codeGenInst(compiler); // TODO (expr ne serait t-il pas un identifier ?)

        GPRegister gpReg = rM.getLastReg();
        compiler.addInstruction(new CMP(new NullOperand(), gpReg));
        compiler.addInstruction(new BEQ(ErrorUtils.nullPointerLabel));

        int fieldOffset = vTM.getCurrOffsetOfField(fieldName);
        fieldIdent.getExpDefinition().setOperand(new RegisterOffset(fieldOffset, gpReg));
        rM.freeReg(gpReg);

        fieldIdent.codeGenInst(compiler);
        // Done
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
