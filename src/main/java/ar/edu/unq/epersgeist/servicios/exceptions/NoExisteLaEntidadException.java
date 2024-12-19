package ar.edu.unq.epersgeist.servicios.exceptions;

public class NoExisteLaEntidadException extends RuntimeException {

    public NoExisteLaEntidadException(String entityName, Long id) {
        super("No existe un/una " + entityName + " con el ID: " + id);
    }

    public NoExisteLaEntidadException(String entityName, String id) {
        super("No existe un/una " + entityName + " con el ID: " + id);
    }

    public NoExisteLaEntidadException(String entityName) {
        super("No existe un/una " + entityName);
    }
}
