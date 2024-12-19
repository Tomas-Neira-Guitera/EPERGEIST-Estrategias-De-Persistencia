package ar.edu.unq.epersgeist.controller.dto.estadistica;

import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituSimpleDTO;
import ar.edu.unq.epersgeist.controller.dto.habilidad.HabilidadDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumSimpleDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearAreaDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearPuntoDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.utils.Snapshot;
import lombok.Getter;
import lombok.Setter;
import org.slf4j.LoggerFactory;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;

@Getter
@Setter
public class EstadisticaDTO {

    private Map<String, Object> sql;
    private Map<String, Object> mongo;
    private Map<String, Object> neo4j;
    private LocalDate fecha;

    public EstadisticaDTO() {
    }

    public EstadisticaDTO(Map<String, Object> sql, Map<String, Object> mongo, Map<String, Object> neo4j, LocalDate fecha) {
        this.sql = sql;
        this.mongo = mongo;
        this.neo4j = neo4j;
        this.fecha = fecha;
    }

    public static EstadisticaDTO desdeModelo(Snapshot snapshot) {

        return new EstadisticaDTO(
                snapshot.getSqlData(),
                snapshot.getMongoData(),
                snapshot.getNeoData(),
                snapshot.getFecha()
        );
    }

}