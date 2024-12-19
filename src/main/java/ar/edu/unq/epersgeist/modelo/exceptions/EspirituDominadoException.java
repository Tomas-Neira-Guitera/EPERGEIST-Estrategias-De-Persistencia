package ar.edu.unq.epersgeist.modelo.exceptions;

public class EspirituDominadoException extends ModeloException {
    public EspirituDominadoException() {
        super("El espiritu se encuentra dominado por otro espiritu");
    }
}
