package ar.edu.unq.epersgeist.modelo.exceptions;

public class ManaNegativoException extends ModeloException {

    public ManaNegativoException() {
        super("El mana de una Medium no puede ser inializado con un valor negativo");
    }
}
