package ar.edu.unq.epersgeist.modelo.exceptions;

public class CantidadNegativaException extends ModeloException {

    public CantidadNegativaException() {
        super("La cantidad no puede ser negativa");
    }
}
