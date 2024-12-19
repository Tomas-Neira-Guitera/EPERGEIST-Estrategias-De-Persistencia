package ar.edu.unq.epersgeist.modelo.exceptions;

public class EspirituNoDebilitadoException extends ModeloException {
    public EspirituNoDebilitadoException() {
        super("El espiritu debe tener una energia menor a 50");
    }
}
