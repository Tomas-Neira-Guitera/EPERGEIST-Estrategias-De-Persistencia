package ar.edu.unq.epersgeist.servicios.exceptions;

public class NoExisteLaHabilidadException extends RuntimeException {

    public NoExisteLaHabilidadException(String nombre) {
        super("No existe la habilidad con el nombre: " + nombre);
    }
}
