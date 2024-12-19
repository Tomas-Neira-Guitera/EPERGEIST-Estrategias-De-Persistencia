package ar.edu.unq.epersgeist.servicios.exceptions;

public class MasDe100KilometrosException extends RuntimeException {
    public MasDe100KilometrosException() {
        super("El espiritu se encuentra a mas de 100 kilometros de distancia");
    }
}
