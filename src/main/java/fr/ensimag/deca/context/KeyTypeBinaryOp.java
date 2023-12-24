package fr.ensimag.deca.context;

public class KeyTypeBinaryOp {
    private String op;
    private Type type1;
    private Type type2;
    public KeyTypeBinaryOp(String op, Type t1, Type t2) {
        this.op = op;
        this.type1 = t1;
        this.type2 = t2;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof KeyTypeBinaryOp)) return false;
        KeyTypeBinaryOp key = (KeyTypeBinaryOp) obj;
        return this.op.equals(key.op) && this.type1.equals(key.type1)
                && this.type2.equals(key.type2);
    }
}
