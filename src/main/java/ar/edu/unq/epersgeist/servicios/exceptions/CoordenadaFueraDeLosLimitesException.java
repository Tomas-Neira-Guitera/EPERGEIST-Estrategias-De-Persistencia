package ar.edu.unq.epersgeist.servicios.exceptions;

public class CoordenadaFueraDeLosLimitesException extends RuntimeException {
    public CoordenadaFueraDeLosLimitesException() {
        super("La coordenada esta fuera de los limites");
    }
}
