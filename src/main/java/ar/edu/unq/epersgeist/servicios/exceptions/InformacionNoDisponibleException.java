package ar.edu.unq.epersgeist.servicios.exceptions;

public class InformacionNoDisponibleException extends RuntimeException {
    public InformacionNoDisponibleException() {
        super("No es posible obtener la informacion solicitada");
    } // TODO handlear
}
