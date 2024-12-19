package ar.edu.unq.epersgeist.servicios.exceptions;

public class ExisteUnaUbicacionEnEseAreaException extends RuntimeException {
    public ExisteUnaUbicacionEnEseAreaException() {
        super("Ya existe una ubicacion en ese area");
    }
    // TODO handlear en el middleware
}
