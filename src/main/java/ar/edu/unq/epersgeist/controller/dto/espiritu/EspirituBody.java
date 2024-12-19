package ar.edu.unq.epersgeist.controller.dto.espiritu;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearPuntoDTO;
import ar.edu.unq.epersgeist.controller.exceptions.TipoDeEspirituInvalidoException;
import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.geo.Point;

@Getter
@Setter
public class EspirituBody {

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    @NotNull(message = "Debe ingresar un tipo de ubicacion valido")
    private String tipoDeEspiritu;

    @NotNull(message = "Debe ingresar una energia")
    @PositiveOrZero(message = "La energia no puede ser negativa")
    @Max(value = 100, message = "La energia no puede ser mayor a 100")
    private int energia;

    @NotNull(message = "Debe ingresar un punto de una ubicacion")
    private CrearPuntoDTO punto;

    public EspirituBody() {

    }

    public EspirituBody(String nombre, String tipoDeEspiritu, int energia, CrearPuntoDTO punto) {
        this.nombre = nombre;
        this.tipoDeEspiritu = tipoDeEspiritu;
        this.energia = energia;
        this.punto = punto;
    }

    public static EspirituBody desdeModelo(Espiritu espiritu, CrearPuntoDTO punto) {
        return new EspirituBody(espiritu.getNombre(), espiritu.getClass().getSimpleName(), espiritu.getEnergia(), punto);
    }

    public Espiritu aModelo() {
        return switch (this.tipoDeEspiritu.toUpperCase()) {
            case "ANGEL" -> new Angel(this.nombre, this.energia);
            case "DEMONIO" -> new Demonio(this.nombre, this.energia);
            default -> throw new TipoDeEspirituInvalidoException();
        };
    }

}