package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.context.Signature;
import fr.ensimag.deca.context.Type;
import fr.ensimag.deca.tools.DecacInternalError;
import fr.ensimag.deca.tools.IndentPrintStream;

public class ListParam extends TreeList<AbstractParam> {

    @Override
    public void decompile(IndentPrintStream s) {
        // TODO
        throw new DecacInternalError("not implemented yet");

    }

    public Signature verifyListDeclParam(DecacCompiler compiler) throws ContextualError {
        Signature sig = new Signature();
        for (AbstractParam param : this.getList()) {
            Type type = param.verifyDeclParam(compiler);
            sig.add(type);
        }
        return sig;
        // Done
    }
}
