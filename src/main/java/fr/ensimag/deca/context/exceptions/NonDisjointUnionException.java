package fr.ensimag.deca.context.exceptions;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.Location;

public class NonDisjointUnionException extends ContextualError {
    public NonDisjointUnionException(Location location){
        super("Opération partielle : union disjointe", location); // TODO: show more dev friendly message
    }
}
