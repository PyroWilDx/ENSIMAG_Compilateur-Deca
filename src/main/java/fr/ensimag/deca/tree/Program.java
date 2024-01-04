package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.codegen.ErrorUtils;
import fr.ensimag.deca.codegen.StackManager;
import fr.ensimag.deca.codegen.VTableManager;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tools.IndentPrintStream;
import fr.ensimag.ima.pseudocode.*;
import fr.ensimag.ima.pseudocode.instructions.*;

import java.io.PrintStream;

import org.apache.commons.lang.Validate;
import org.apache.log4j.Logger;

/**
 * Deca complete program (class definition plus main block)
 *
 * @author gl47
 * @date 01/01/2024
 */
public class Program extends AbstractProgram {
    private static final Logger LOG = Logger.getLogger(Program.class);

    public Program(ListDeclClass classes, AbstractMain main) {
        Validate.notNull(classes);
        Validate.notNull(main);
        this.classes = classes;
        this.main = main;
    }

    public ListDeclClass getClasses() {
        return classes;
    }

    public AbstractMain getMain() {
        return main;
    }

    private ListDeclClass classes;
    private AbstractMain main;

    @Override
    public void verifyProgram(DecacCompiler compiler) throws ContextualError {
        LOG.debug("verify program: start");
        classes.verifyListClass(compiler);
//        classes.verifyListClassMembers(compiler);
//        classes.verifyListClassBody(compiler);
        main.verifyMain(compiler);
        LOG.debug("verify program: end");
        // TODO (Avec Objet -> Enlever Comm)
    }

    @Override
    public void codeGenProgram(DecacCompiler compiler) {
        StackManager sM = compiler.getStackManager();
        VTableManager vTM = compiler.getVTableManager();

        compiler.addComment("VTable of Object");
        compiler.addInstruction(new LOAD(new NullOperand(), Register.R0));
        DAddr nAddr = sM.getGbOffsetAddr();
        compiler.addInstruction(new STORE(Register.R0, nAddr));
        sM.incrStackSize();

        // TODO (à décommenter quand le label equals de object sera défini)
//        compiler.addInstruction(
//                new LOAD(new LabelOperand("code.Object.equals"), Register.R0));
//        DAddr eAddr = sM.getGbOffsetAddr();
//        compiler.addInstruction(new STORE(Register.R0, eAddr));
//        sM.incrStackSize();

        classes.codeGenListVTable(compiler);
        classes.codeGenListDeclClass(compiler);

        compiler.addComment("Start of Main Program");
        int iTSTO = compiler.getProgramLineCount();
        compiler.addComment("Main Program");
        main.codeGenMain(compiler);
        compiler.addInstruction(new HALT());
        compiler.addComment("End of Main Program");

        compiler.addInstruction(iTSTO,
                new TSTO(sM.getMaxStackSize()));
        compiler.addInstruction(iTSTO + 1,
                new BOV(ErrorUtils.stackOverflowLabel));
        compiler.addInstruction(iTSTO + 2,
                new ADDSP(sM.getMaxStackSize() + 1));

        compiler.addComment("");

        compiler.addComment("Start of Error Labels");
        ErrorUtils.codeGenError(compiler,
                "Error: Stack Overflow",
                ErrorUtils.stackOverflowLabel);
        ErrorUtils.codeGenError(compiler,
                "Error: Division by 0",
                ErrorUtils.divBy0Label);
        ErrorUtils.codeGenError(compiler,
                "Error: Input/Output Error",
                ErrorUtils.ioErrLabel);
        compiler.addComment("End of Error Labels");
        // TODO (Avec Objet)
    }

    @Override
    public void decompile(IndentPrintStream s) {
        getClasses().decompile(s);
        getMain().decompile(s);
    }

    @Override
    protected void iterChildren(TreeFunction f) {
        classes.iter(f);
        main.iter(f);
    }

    @Override
    protected void prettyPrintChildren(PrintStream s, String prefix) {
        classes.prettyPrint(s, prefix, false);
        main.prettyPrint(s, prefix, true);
    }
}
