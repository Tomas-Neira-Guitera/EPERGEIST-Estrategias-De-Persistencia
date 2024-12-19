package ar.edu.unq.epersgeist.modelo.exceptions;

public class EnergiaFueraDeLosLimitesException extends ModeloException {

    public EnergiaFueraDeLosLimitesException() {
        super("El nivel de energia debe ser un valor entre 0 y 100");
    }
}
