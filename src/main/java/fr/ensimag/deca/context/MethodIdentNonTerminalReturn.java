package fr.ensimag.deca.context;

public class MethodIdentNonTerminalReturn {
    private Signature sig;
    private Type type;

    public MethodIdentNonTerminalReturn(Signature sig, Type type) {
        this.sig = sig;
        this.type = type;
    }

    public Signature getSignature() {
        return sig;
    }

    public Type getType() {
        return type;
    }
}
