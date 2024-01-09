package fr.ensimag.deca.tree;

import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.deca.context.*;
import fr.ensimag.deca.tools.IndentPrintStream;

import java.util.Iterator;

public class RValueStar extends TreeList<AbstractExpr> {

    public RValueStar() {
        super();
    }

    public RValueStar(ListExpr args) {
        for (AbstractExpr expr : args.getList()) {
            this.add(expr);
        }
    }

    @Override
    public void decompile(IndentPrintStream s) {
        Iterator<AbstractExpr> iter = this.getList().iterator();
        while (iter.hasNext()) {
            iter.next().decompile(s);
            if (iter.hasNext()) {
                s.print(", ");
            }
        }
    }

    public void verifyRValueStar(DecacCompiler compiler,
                                 EnvironmentExp localEnv,
                                 ClassDefinition currentClass,
                                 Signature sig) throws ContextualError {
        if (this.getList().isEmpty()) {
            if (!sig.isEmpty()) {
                throw new ContextualError("To few arguments, expected type : '"
                        + sig.getFirst().getName() + "'", getLocation());
            }
            return;
        }
        AbstractExpr rValue = getList().get(0);
        Type expectedType = sig.getFirst();
        if (expectedType == null) {
            throw new ContextualError("Too many arguments", getLocation());
        }
        rValue.verifyRValue(compiler, localEnv, currentClass, expectedType);

        RValueStar rValueStarWithoutFirst = this.copyWithoutFirst();
        Signature sigWithoutFirst = sig.copyWithoutFirst();
        rValueStarWithoutFirst.verifyRValueStar(compiler, localEnv, currentClass, sigWithoutFirst);
    }

    public RValueStar copyWithoutFirst() {
        RValueStar newRValueStar = new RValueStar();
        for (AbstractExpr rValue : this.getList().subList(1, getList().size() - 1)) {
            newRValueStar.add(rValue);
        }
        return newRValueStar;
    }
}
