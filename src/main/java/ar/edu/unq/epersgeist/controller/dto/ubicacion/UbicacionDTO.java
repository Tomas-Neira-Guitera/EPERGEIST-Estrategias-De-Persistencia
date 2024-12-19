package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.controller.exceptions.TipoDeUbicacionInvalidaException;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UbicacionDTO {

    @NotNull(message = "Debe ingresar un id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    @NotNull(message = "Debe ingresar una energia")
    @PositiveOrZero(message = "La energia no puede ser negativa")
    @Max(value = 100, message = "La energia no puede ser mayor a 100")
    private Integer energia;

    @NotNull(message = "Debe ingresar un tipo de ubicacion valido")
    private String tipoUbicacion;

    public UbicacionDTO() { // Constructor predeterminado
    }

    public UbicacionDTO(Long id, String nombre, Integer energia, String tipoUbicacion) {
        this.id = id;
        this.nombre = nombre;
        this.energia = energia;
        this.tipoUbicacion = tipoUbicacion;
    }

    public static UbicacionDTO desdeModelo(Ubicacion ubicacion) {
        return new UbicacionDTO(ubicacion.getId(), ubicacion.getNombre(), ubicacion.getEnergia(), ubicacion.getClass().getSimpleName());
    }

    public Ubicacion aModelo() {
        return switch (this.tipoUbicacion.toUpperCase()) {
            case "CEMENTERIO" -> new Cementerio(this.nombre, this.energia, this.id);
            case "SANTUARIO" -> new Santuario(this.nombre, this.energia, this.id);
            default -> throw new TipoDeUbicacionInvalidaException();
        };
    }

}