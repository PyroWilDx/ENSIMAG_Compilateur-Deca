package fr.ensimag.deca.tree;

import fr.ensimag.deca.context.*;
import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.exceptions.NonDisjointUnionException;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;

/**
 * @author gl47
 * @date 01/01/2024
 */
public class DeclVar extends AbstractDeclVar {

    final private AbstractIdentifier type;
    final private AbstractIdentifier varName;
    final private AbstractInitialization initialization;

    public DeclVar(AbstractIdentifier type, AbstractIdentifier varName, AbstractInitialization initialization) {
        Validate.notNull(type);
        Validate.notNull(varName);
        Validate.notNull(initialization);
        this.type = type;
        this.varName = varName;
        this.initialization = initialization;
    }

    @Override
    protected void verifyDeclVar(DecacCompiler compiler,
                                 EnvironmentExp localEnv, ClassDefinition currentClass) throws ContextualError {
        EnvironmentExp declEnv = new EnvironmentExp(null);
        try {
            declEnv.declare(varName.getName(), new VariableDefinition(compiler.environmentType.defOfType(type.getName()).getType(), this.getLocation()));
            localEnv.disjointUnion(declEnv, this.getLocation());
        } catch(NonDisjointUnionException e) {
            System.out.println(e.getMessage() + "Variable non délarée..."); // cf regle 3.17
        } catch(EnvironmentExp.DoubleDefException e) { // pas possible d'avoir cette erreur mais idea pas content
            throw new UnknownError();
        }
        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void codeGenDeclVar(DecacCompiler compiler) {
        initialization.codeGenInit(compiler, varName.getExpDefinition().getOperand());
        // TODO
    }

    @Override
    public void decompile(IndentPrintStream s) {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        type.iter(f);
        varName.iter(f);
        initialization.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        type.prettyPrint(s, prefix, false);
        varName.prettyPrint(s, prefix, false);
        initialization.prettyPrint(s, prefix, true);
    }
}
