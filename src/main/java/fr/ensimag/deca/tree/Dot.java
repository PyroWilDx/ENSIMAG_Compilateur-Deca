package fr.ensimag.deca.tree;


import fr.ensimag.deca.DecacCompiler;
import fr.ensimag.ima.pseudocode.DVal;
import fr.ensimag.ima.pseudocode.GPRegister;
import fr.ensimag.ima.pseudocode.instructions.MUL;
/**
 * @author haydar anass
 * @date 08/01/2024
 */
public class Dot extends AbstractDot{
    public Dot(AbstractExpr classMere, AbstractExpr champ){
        super(classMere,champ);
    }
    void codeGenDot(DecacCompiler compiler, DVal valReg, GPRegister saveReg){
        //TODO
    }
}
