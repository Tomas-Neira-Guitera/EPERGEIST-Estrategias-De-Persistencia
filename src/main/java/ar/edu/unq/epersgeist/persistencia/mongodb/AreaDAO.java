package ar.edu.unq.epersgeist.persistencia.mongodb;


import ar.edu.unq.epersgeist.modelo.ubicacion.Area;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.awt.*;
import java.util.Optional;

@Repository
public interface AreaDAO extends MongoRepository<Area, String> {

    @Query("{ 'poligono' : { $geoIntersects : { $geometry : ?0 } } }")
    Optional<Area> encontrarAreaEnPoligono(GeoJsonPolygon poligono);

    @Query("{ 'poligono' : { $geoIntersects : { $geometry : ?0 } } }")
    Optional<Area> encontrarElAreaDelPunto(GeoJsonPoint punto);

    @Query(value = "{ 'idUbicacion' : ?0 }", delete = true)
    void eliminarPorIdDeUbicacion(Long id);

    @Query("{'idUbicacion' : ?0}")
    Optional<Area> encontrarPorIdDeUbicacion(Long id);
}
