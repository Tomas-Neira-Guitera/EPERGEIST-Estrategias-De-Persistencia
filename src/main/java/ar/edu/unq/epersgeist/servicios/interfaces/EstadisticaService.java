package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.utils.Snapshot;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface EstadisticaService {

    public Ubicacion ubicacionMasDominada(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    public Espiritu espirituMasDominante(LocalDateTime fechaInicio, LocalDateTime fechaFin);
    void crearSnapshot();
    Snapshot obtenerSnapshot(LocalDate fecha);

}
