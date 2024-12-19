package ar.edu.unq.epersgeist.servicios.exceptions;

public class HabilidadesNoConectadasException extends RuntimeException {
    public HabilidadesNoConectadasException() {
        super("Las habilidades no estan conectadas");
    }
}
