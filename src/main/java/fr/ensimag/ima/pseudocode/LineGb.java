package fr.ensimag.ima.pseudocode;

import java.io.PrintStream;

public class LineGb extends AbstractLine {

    private final String gbAsm;

    public LineGb(String gbAsm) {
        this.gbAsm = gbAsm;
    }

    @Override
    void display(PrintStream s) {
        s.println("\t" + gbAsm);
    }
}
