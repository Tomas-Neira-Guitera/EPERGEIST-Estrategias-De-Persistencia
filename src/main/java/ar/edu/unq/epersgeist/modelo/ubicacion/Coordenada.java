package ar.edu.unq.epersgeist.modelo.ubicacion;


import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "coordenadasIndex")
public class Coordenada {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @GeoSpatialIndexed(name = "punto")
    private GeoJsonPoint punto;

    private Long entidadId;

    private TipoDeEntidad tipoDeEntidad;

    public Coordenada(GeoJsonPoint punto, Long entidadId, TipoDeEntidad tipoDeEntidad) {
        this.punto = punto;
        this.entidadId = entidadId;
        this.tipoDeEntidad = tipoDeEntidad;
    }
}
