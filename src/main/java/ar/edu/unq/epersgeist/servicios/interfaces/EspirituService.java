package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.controller.dto.espiritu.ActualizarEspirituDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.utils.Direccion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;


import java.util.List;

public interface EspirituService {
    Espiritu crear(Espiritu espiritu);
    Espiritu crear(Espiritu espiritu, GeoJsonPoint punto);
    Espiritu recuperar(Long id);
    List<Espiritu> recuperarTodos();
    void actualizar(Espiritu espiritu);
    void actualizar(ActualizarEspirituDTO actualizarEspirituDTO);
    void eliminar(Long id);
    List<Demonio> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina);
    Medium conectar(Long espirituId, Long mediumId);
    void agregarHabilidad(Long espirituId, Long habilidadId);
    void dominar(Long espirituDominanteId, Long espirituADominarId);

}