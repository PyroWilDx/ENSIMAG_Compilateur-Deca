package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;

/**
 * @author haydar anass
 * @date 08/01/2024
 * this class is used in the parser in the DOT part in "select_expr" . In other words it's the one
 * used when we want to access a field of a class like "pingolin.bec"
 */
public class Dot extends AbstractDot {

    public Dot(AbstractExpr classExpr, AbstractExpr champ) {
        super(classExpr, champ);
    }

}
