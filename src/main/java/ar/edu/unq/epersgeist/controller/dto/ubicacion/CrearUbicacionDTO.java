package ar.edu.unq.epersgeist.controller.dto.ubicacion;


import ar.edu.unq.epersgeist.controller.exceptions.TipoDeUbicacionInvalidaException;
import ar.edu.unq.epersgeist.modelo.ubicacion.Cementerio;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearUbicacionDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    @NotNull(message = "Debe ingresar una energia")
    @PositiveOrZero(message = "La energia no puede ser negativa")
    @Max(value = 100, message = "La energia no puede ser mayor a 100")
    private Integer energia;

    @NotNull(message = "Debe ingresar un tipo de ubicacion valido")
    private String tipoUbicacion;

    @NotNull(message = "Debe ingresar un area")
    private CrearAreaDTO area;

    public CrearUbicacionDTO() {
    }

    public CrearUbicacionDTO(String nombre, Integer energia, String tipoUbicacion, CrearAreaDTO area) {
        this.nombre = nombre;
        this.energia = energia;
        this.tipoUbicacion = tipoUbicacion;
        this.area = area;
    }

    public static CrearUbicacionDTO desdeModelo(Ubicacion ubicacion, CrearAreaDTO area) {
        return new CrearUbicacionDTO(ubicacion.getNombre(), ubicacion.getEnergia(), ubicacion.getClass().getSimpleName(), area);
    }

    public Ubicacion aModelo(){
        return switch (this.tipoUbicacion.toUpperCase()) {
            case "CEMENTERIO" -> new Cementerio(this.nombre, this.energia);
            case "SANTUARIO" -> new Santuario(this.nombre, this.energia);
            default -> throw new TipoDeUbicacionInvalidaException();
        };
    }

}
