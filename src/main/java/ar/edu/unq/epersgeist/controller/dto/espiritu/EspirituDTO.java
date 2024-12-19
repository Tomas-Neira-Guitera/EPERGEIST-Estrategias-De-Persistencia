package ar.edu.unq.epersgeist.controller.dto.espiritu;


import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumSimpleDTO;
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
public class EspirituDTO {

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

    private MediumSimpleDTO mediumSimpleDTO;

    public EspirituDTO() {

    }

    public EspirituDTO(int energia, int nivelDeConexion, String tipoDeEspiritu, String nombre, Long id, MediumSimpleDTO mediumSimpleDTO, UbicacionDTO ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDeEspiritu = tipoDeEspiritu;
        this.ubicacion = ubicacion;
        this.energia = energia;
        this.nivelDeConexion = nivelDeConexion;
        this.mediumSimpleDTO = mediumSimpleDTO;
    }

    public EspirituDTO(int energia, int nivelDeConexion, String tipoDeEspiritu, String nombre, Long id, UbicacionDTO ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.tipoDeEspiritu = tipoDeEspiritu;
        this.ubicacion = ubicacion;
        this.energia = energia;
        this.nivelDeConexion = nivelDeConexion;
    }

    public static EspirituDTO desdeModelo(Espiritu espiritu) {

        if (espiritu.getMedium() != null){
            return new EspirituDTO(
                    espiritu.getEnergia(),
                    espiritu.getNivelDeConexion(),
                    espiritu.getClass().getSimpleName(),
                    espiritu.getNombre(),
                    espiritu.getId(),
                    MediumSimpleDTO.desdeModelo(espiritu.getMedium()),
                    UbicacionDTO.desdeModelo(espiritu.getUbicacion())
            );
        }
        return new EspirituDTO(
                espiritu.getEnergia(),
                espiritu.getNivelDeConexion(),
                espiritu.getClass().getSimpleName(),
                espiritu.getNombre(),
                espiritu.getId(),
                UbicacionDTO.desdeModelo(espiritu.getUbicacion())
        );
    }

    public Espiritu aModelo() {
        return switch (this.tipoDeEspiritu.toUpperCase()) {
            case "ANGEL" -> new Angel(this.nombre, this.energia, this.nivelDeConexion, this.ubicacion.aModelo(), this.mediumSimpleDTO.aModelo(), this.id);
            case "DEMONIO" -> new Demonio(this.nombre, this.energia, this.nivelDeConexion, this.ubicacion.aModelo(), this.mediumSimpleDTO.aModelo(), this.id);
            default -> throw new TipoDeEspirituInvalidoException();
        };
    }
}
