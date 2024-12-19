package ar.edu.unq.epersgeist.modelo.exceptions;

public abstract class ModeloException extends RuntimeException {
    public ModeloException(String message) {
        super(message);
    }
}
