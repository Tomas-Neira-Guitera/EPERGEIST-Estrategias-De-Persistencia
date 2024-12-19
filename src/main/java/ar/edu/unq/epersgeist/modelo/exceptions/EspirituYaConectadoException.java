package ar.edu.unq.epersgeist.modelo.exceptions;

public class EspirituYaConectadoException extends ModeloException {

    public EspirituYaConectadoException() {
        super("El espiritu ya se encuentra conectado a un medium");
    }
}
