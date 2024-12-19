package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.exceptions.TipoDeEspirituInvalidoException;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class EspirituSimpleDTO {
    @NotNull(message = "Debe ingresar un id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    @NotNull(message = "Debe ingresar un tipo de ubicacion valido")
    private String tipoDeEspiritu;

    @NotNull(message = "Debe ingresar una ubicacion")
    private UbicacionDTO ubicacion;

    @NotNull(message = "Debe ingresar una energia")
    @PositiveOrZero(message = "La energia no puede ser negativa")
    @Max(value = 100, message = "La energia no puede ser mayor a 100")
    private int energia;

    @NotNull(message = "Debe ingresar un nivel de conexion")
    @PositiveOrZero(message = "El nivel de conexion no puede ser negativo")
    @Max(value = 100, message = "El nivel de conexion no puede ser mayor a 100")
    private int nivelDeConexion;

    public EspirituSimpleDTO(String nombre, Long id, String tipoDeEspiritu, UbicacionDTO ubicacion, int nivelDeConexion, int energia) {
        this.nombre = nombre;
        this.id = id;
        this.tipoDeEspiritu = tipoDeEspiritu;
        this.ubicacion = ubicacion;
        this.nivelDeConexion = nivelDeConexion;
        this.energia = energia;
    }

    public static EspirituSimpleDTO desdeModelo(Espiritu espiritu) {
        return new EspirituSimpleDTO(
                espiritu.getNombre(),
                espiritu.getId(),
                espiritu.getClass().getSimpleName(),
                UbicacionDTO.desdeModelo(espiritu.getUbicacion()),
                espiritu.getNivelDeConexion(),
                espiritu.getEnergia());
    }

    public Espiritu aModelo(){
        return switch (this.tipoDeEspiritu.toUpperCase()) {
            case "ANGEL" -> new Angel(this.id, this.nombre, this.ubicacion.aModelo(), this.energia, this.nivelDeConexion);
            case "DEMONIO" -> new Demonio(this.id, this.nombre, this.ubicacion.aModelo(), this.energia, this.nivelDeConexion);
            default -> throw new TipoDeEspirituInvalidoException();
        };
    }

}
