package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.TipoDeCondicion;

import ar.edu.unq.epersgeist.modelo.condicion.Condicion;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;


import java.util.List;
import java.util.Set;

public interface HabilidadService {

    HabilidadNode crear(HabilidadNode habilidadNode);

    HabilidadNode recuperar(String nombreHabilidad);

    List<HabilidadNode> recuperarTodos();

    void descubrirHabilidad(String nombreHabilidadOrigen, String nombreHabilidadDestino, Condicion condicion);

    void evolucionar(Long espirituId);

    Set<HabilidadNode> habilidadesConectadas(String nombreHabilidad);

    Set<HabilidadNode> habilidadesPosibles(Long espirituId);

    List<HabilidadNode> caminoMasRentable(String nombreHabilidadOrigen, String nombreHabilidadDestino, Set<TipoDeCondicion> condiciones);

    List<HabilidadNode> caminoMasMutable(Long espirituId, String nombreHabilidad);

    List<HabilidadNode> caminoMenosMutable(String nombreHabilidad, Long espirituId);

}