package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.codegen.GameBoy;

/**
 * Immediate operand representing an integer.
 * 
 * @author Ensimag
 * @date 01/01/2024
 */
public class ImmediateInteger extends DVal {
    private int value;

    public ImmediateInteger(int value) {
        super();
        this.value = value;
    }

    public void decrValue() {
        value--;
    }

    @Override
    public String toString() {
        return GameBoy.getImmToken() + value;
    }
}
