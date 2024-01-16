package fr.ensimag.ima.pseudocode;

import fr.ensimag.deca.codegen.GameBoy;

/**
 * Immediate operand containing a float value.
 * 
 * @author Ensimag
 * @date 01/01/2024
 */
public class ImmediateFloat extends DVal {
    private float value;

    public ImmediateFloat(float value) {
        super();
        this.value = value;
    }

    public void decrValue() {
        value--;
    }

    @Override
    public String toString() {
        if (GameBoy.doCp) return GameBoy.getImmToken() + ((int) value);
        return GameBoy.getImmToken() + Float.toHexString(value);
    }
}
