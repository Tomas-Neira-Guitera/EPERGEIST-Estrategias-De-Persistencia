package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.controller.exceptions.PoligonoIncompletoException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import java.util.List;

@Getter
@Setter
public class CrearAreaDTO {

    private List<CrearPuntoDTO> area;

    public CrearAreaDTO() {
    }

    public CrearAreaDTO(List<CrearPuntoDTO> area) {
        this.area = area;
    }

    public static CrearAreaDTO desdeModelo(List<CrearPuntoDTO> area) {
        return new CrearAreaDTO(area);
    }

    public Area aModelo() {

        if (area.size() < 4) {
            throw new PoligonoIncompletoException("El área debe contener al menos 4 puntos para ser un polígono.");
        }

        List<GeoJsonPoint> areaPoints = area.stream().map(CrearPuntoDTO::aModelo).toList();


        GeoJsonPoint first = areaPoints.get(0);
        GeoJsonPoint second = areaPoints.get(1);
        GeoJsonPoint third = areaPoints.get(2);
        GeoJsonPoint fourth = areaPoints.get(3);

        GeoJsonPoint[] others = areaPoints.subList(4, areaPoints.size()).toArray(new GeoJsonPoint[0]);

        return new Area(first, second, third, fourth, others);
    }

}
