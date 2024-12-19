package ar.edu.unq.epersgeist.servicios.exceptions;

public class EntidadConNombreYaExistenteException extends RuntimeException {

    public EntidadConNombreYaExistenteException(String entityName) {
        super("Ya existe un/una " + entityName + " con ese nombre");
    }
}