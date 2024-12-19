package ar.edu.unq.epersgeist.servicios.exceptions;

import java.time.LocalDate;

public class NoSeEncontroSnapshotException extends RuntimeException {
    public NoSeEncontroSnapshotException(LocalDate fecha) {
        super("No se encontro snapshot para la fecha " + fecha);
    }
}
