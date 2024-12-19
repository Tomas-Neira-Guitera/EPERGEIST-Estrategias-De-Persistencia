package ar.edu.unq.epersgeist.modelo.ubicacion;


import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.core.geo.GeoJsonPolygon;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private GeoJsonPolygon poligono;

    private Long idUbicacion;

    public Area(GeoJsonPoint first, GeoJsonPoint second, GeoJsonPoint third, GeoJsonPoint fourth, GeoJsonPoint... others) {
        this.poligono = new GeoJsonPolygon(first, second, third, fourth, others);
    }
}