package ar.edu.unq.epersgeist.controller.exceptions;

import ar.edu.unq.epersgeist.modelo.exceptions.ModeloException;

public class TipoDeUbicacionInvalidaException extends ModeloException {

    public TipoDeUbicacionInvalidaException() {
        super("El tipo de ubicacion debe ser de SANTUARIO o CEMENTERIO");
    }
}
