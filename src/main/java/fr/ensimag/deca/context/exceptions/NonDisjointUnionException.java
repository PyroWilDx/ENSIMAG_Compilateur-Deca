package fr.ensimag.deca.context.exceptions;

import fr.ensimag.deca.context.ContextualError;
import fr.ensimag.deca.tree.Location;

public class NonDisjointUnionException extends ContextualError {
    public NonDisjointUnionException(Location location){
        super("Variable déjà déclaré", location); // TODO: show more dev friendly message
    }
}
