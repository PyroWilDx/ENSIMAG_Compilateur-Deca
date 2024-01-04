package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.TypeDefinition;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.deca.tools.SymbolTable;
import fr.ensimag.ima.pseudocode.DAddr;
import fr.ensimag.ima.pseudocode.Register;
import fr.ensimag.ima.pseudocode.instructions.LEA;
import fr.ensimag.ima.pseudocode.instructions.STORE;

import java.io.PrintStream;

/**
 * Declaration of a class (<code>class name extends superClass {members}<code>).
 *
 * @author gl47
 * @date 01/01/2024
 */
public class DeclClass extends AbstractDeclClass {

    private final AbstractIdentifier name;
    private final AbstractIdentifier superClass;
    private final ListDeclField fields;
    private final ListDeclMethod methods;

    public DeclClass(AbstractIdentifier name, AbstractIdentifier superClass, ListDeclField fields, ListDeclMethod methods) {
        this.name = name;
        this.superClass = superClass;
        this.fields = fields;
        this.methods = methods;
    }

    public AbstractIdentifier getName() {
        return name;
    }

    public AbstractIdentifier getSuperClass() {
        return superClass;
    }

    public ListDeclMethod getMethods() {
        return methods;
    }

    public ListDeclField getFields() {
        return fields;
    }

    @Override
    public void decompile(IndentPrintStream s) {
        s.print("class { ... A FAIRE ... }");
    }

    @Override
    protected void verifyClass(DecacCompiler compiler) throws ContextualError {
        TypeDefinition defSuperClass = compiler.environmentType.get(this.superClass.getName());
        if (defSuperClass == null) {
            throw new ContextualError("Undeclared identifier",
                    getLocation());
        }
        if (!defSuperClass.isClass()) {
            throw new ContextualError("A class identifier is required",
                    getLocation());
        }
        if (!compiler.environmentType.declareClasse(this.name.getName(),
                this.superClass.getClassDefinition(), this.getLocation())) {
            throw new ContextualError("Class or type already exists.", this.getLocation());
        }
        // Done
    }

    @Override
    protected void verifyClassMembers(DecacCompiler compiler) throws ContextualError {

        //throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    protected void verifyClassBody(DecacCompiler compiler) throws ContextualError {
        throw new UnsupportedOperationException("not yet implemented");
    }

    @Override
    public void codeGenVTable(DecacCompiler compiler) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        DAddr startAddr = sM.getGbOffsetAddr();
        vTM.addClass(name.getName().getName(), startAddr);
        compiler.addInstruction(
                new LEA(vTM.getAddrOfClass(superClass.getName().getName()), Register.R0));
        compiler.addInstruction(new STORE(Register.R0, startAddr));
        sM.incrVTableCpt();

        methods.codeGenVTable(compiler, name);
        // Done
    }

    @Override
    public void codeGenDeclClass(DecacCompiler compiler) {

    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        fields.prettyPrintChildren(s, prefix);
        methods.prettyPrintChildren(s, prefix);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        throw new UnsupportedOperationException("Not yet supported");
    }

}
