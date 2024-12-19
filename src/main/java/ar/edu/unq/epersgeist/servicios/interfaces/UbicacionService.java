package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.ActualizarUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;

import java.util.List;

public interface UbicacionService {
    Ubicacion crear(Ubicacion ubicacion, Area area);
    Ubicacion recuperar(Long id);
    List<Ubicacion> recuperarTodos();
    void actualizar(Ubicacion ubicacion);
    void actualizar(ActualizarUbicacionDTO ubicacion);
    void eliminar(Long id);
    List<Espiritu> espiritusEn(Long id);
    List<Medium> mediumsSinEspiritusEn(Long id);
}
