package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

public class Selection extends AbstractLValue{
    private AbstractExpr expr;
    private AbstractIdentifier fieldIdent;

    public Selection(AbstractExpr expr, AbstractIdentifier fieldIdent) {
        this.expr = expr;
        this.fieldIdent = fieldIdent;
    }

    @Override
    public Type verifyLValue(DecacCompiler compiler, EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        Type expressiontype = this.expr.verifyExpr(compiler, localEnv, currentClass);

        TypeDefinition exprTypeDef = compiler.environmentType.get(expressiontype.getName());
        ClassDefinition exprClassDef = exprTypeDef.asClassDefinition("Expression type must be a class.", getLocation());
        if (!expressiontype.isClass()) {
            throw new DecacInternalError("class and not class, problem");
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
                        "' must be a subtype of '" + currentClass.getType().getName(), getLocation());
            }
            // la classe courante doit être sous classe de la classe contenant la méthode.
            if (!compiler.environmentType.subtype(currentClass.getType(), fieldContainingClass.getType())) {
                throw new ContextualError("'" + this.fieldIdent.getName() +
                        "' is protected, hence '" + currentClass.getType().getName() +
                        "' must be a subtype of '" + fieldContainingClass.getType().getName(), getLocation());
            }
        }
        return type;
        // Done
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
