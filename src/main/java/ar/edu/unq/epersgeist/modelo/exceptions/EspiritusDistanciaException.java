package ar.edu.unq.epersgeist.modelo.exceptions;

public class EspiritusDistanciaException extends ModeloException {
    public EspiritusDistanciaException() {
        super("Los espiritus deben encontrarse a una distancia de entre 2 a 5 km");
    }
}
