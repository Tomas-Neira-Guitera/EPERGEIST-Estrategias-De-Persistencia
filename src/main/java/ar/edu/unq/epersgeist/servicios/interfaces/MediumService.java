package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.controller.dto.medium.ActualizarMediumDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;


import java.util.List;

public interface MediumService {
    void crear(Medium medium);
    Medium crear(Medium medium, GeoJsonPoint punto);
    Medium crear(Medium medium, Long ubicacionId);
    Medium recuperar(Long id);
    List<Medium> recuperarTodos();
    void actualizar(Medium medium);
    void actualizar(ActualizarMediumDTO medium);
    void eliminar(Long id);
    void descansar(Long mediumId);
    void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar);
    List<Espiritu> espiritus(Long mediumId);
    Espiritu invocar(Long mediumId, Long espirituId);
    void mover(Long mediumId, Double latitud, Double longitud);
}
