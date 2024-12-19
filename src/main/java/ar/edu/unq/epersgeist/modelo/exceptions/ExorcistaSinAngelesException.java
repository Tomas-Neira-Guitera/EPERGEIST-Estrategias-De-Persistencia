package ar.edu.unq.epersgeist.modelo.exceptions;

import ar.edu.unq.epersgeist.modelo.medium.Medium;

public class ExorcistaSinAngelesException extends ModeloException {

    public ExorcistaSinAngelesException(Medium medium) {
        super("El medium " + medium.getNombre() + " necesita estar conectado a al menos un espíritu ángel para poder exorcizar");
    }
}
