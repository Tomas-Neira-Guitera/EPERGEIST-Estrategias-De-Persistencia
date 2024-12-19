package ar.edu.unq.epersgeist.controller.exceptions;

import ar.edu.unq.epersgeist.modelo.exceptions.ModeloException;

public class TipoDeEspirituInvalidoException extends ModeloException {
    public TipoDeEspirituInvalidoException() {
        super("El tipo de espiritu es invalido");
    }
}
