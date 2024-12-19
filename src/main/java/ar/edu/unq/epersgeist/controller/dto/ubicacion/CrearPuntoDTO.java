package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

@Getter
@Setter
public class CrearPuntoDTO {

    @NotNull(message = "Debe ingresar un valor para x")
    private Double x;

    @NotNull(message = "Debe ingresar un valor para y")
    private Double y;

    public CrearPuntoDTO() {
    }

    public CrearPuntoDTO(Double x, Double y) {
        this.x = x;
        this.y = y;
    }

    public static CrearPuntoDTO desdeModelo(GeoJsonPoint point) {
        return new CrearPuntoDTO(point.getX(), point.getY());
    }

    public GeoJsonPoint aModelo() {
        return new GeoJsonPoint(this.x, this.y);
    }


}
