package fr.ensimag.ima.pseudocode;

import org.apache.commons.lang.Validate;

/**
 * Label used as operand
 *
 * @author Ensimag
 * @date 01/01/2024
 */
public class LabelOperand extends DVal {
    public Label getLabel() {
        return label;
    }

    private final Label label;
    
    public LabelOperand(Label label) {
        super();
        Validate.notNull(label);
        this.label = label;
    }

    public LabelOperand(String labelStr) {
        this(new Label(labelStr));
    }

    @Override
    public String toString() {
        return label.toString();
    }

}
