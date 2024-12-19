package ar.edu.unq.epersgeist.modelo.exceptions;

public class NoExisteUnaUbicacionEnEstePunto extends RuntimeException {
    public NoExisteUnaUbicacionEnEstePunto() {
        super("No existe una ubicación en la coordenada dada");
    }
    // TODO handlear en el middleware
}
