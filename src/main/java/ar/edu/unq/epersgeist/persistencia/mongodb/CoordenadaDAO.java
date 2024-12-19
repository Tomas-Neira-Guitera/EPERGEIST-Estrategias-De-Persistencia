package ar.edu.unq.epersgeist.persistencia.mongodb;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.DeleteQuery;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CoordenadaDAO extends MongoRepository<Coordenada, String> {

    @Query("{ 'punto': ?0, 'tipoDeEntidad': ?1 }")
    Optional<Coordenada> encontrarEnPunto(Point punto, TipoDeEntidad espiritu);

    @DeleteQuery("{ 'entidadId' : ?0, 'tipoDeEntidad' : ?1 }")
    void eliminarCoordenadaDeEntidadConId(Long id, TipoDeEntidad tipo);

    @Query("{ 'entidadId' : ?0, 'tipoDeEntidad' : ?1 }")
    Optional<Coordenada> obtenerCoordenadaDeEntidadConId(Long id, TipoDeEntidad tipo);

    @Query("{ 'id': ?1, 'punto': { '$near': { '$geometry': ?0, '$minDistance': 2000 , '$maxDistance': 5000 } } }")
    List<Coordenada> encontrarCoordenadasDentroDeRangoConId(GeoJsonPoint punto, String idCoordenada);

    @Query("{ 'entidadId' : { $in: ?0 }, 'tipoDeEntidad' : ?1 }")
    List<Coordenada> obtenerCoordenadasDeEntidadesConIds(List<Long> ids, TipoDeEntidad tipo);

    @Query("{ 'id': ?2, 'punto': { '$near': { '$geometry': { 'type': 'Point', 'coordinates': [ ?0, ?1 ] }, '$minDistance': 2000 , '$maxDistance': 5000 } } }")
    List<Coordenada> encontrarCoordenadasDentroDeRangoConId(double longitud, double latitud, String idCoordenada);

    @Query("{'entidadId' : ?2, 'tipoDeEntidad' : ?3, 'punto': { '$near' : { '$geometry': { 'type': 'Point', 'coordinates': [ ?0, ?1 ] }, '$maxDistance': 100000 } } }")
    Optional<Coordenada> encontrarEntidadCercana(double longitud, double latitud , Long entidadId, TipoDeEntidad tipo);

}
