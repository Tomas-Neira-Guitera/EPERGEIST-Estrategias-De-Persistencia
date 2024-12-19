package ar.edu.unq.epersgeist.controller.dto.medium;

import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituSimpleDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class MediumDTO{

    @NotNull(message = "Debe ingresar un id")
    private Long id;

    @NotNull(message = "Debe ingresar un nombre")
    @NotBlank(message = "El nombre no puede estar vacio")
    private String nombre;

    @NotNull(message = "Debe ingresar una mana")
    @PositiveOrZero(message = "El mana no puede ser negativo")
    private int mana;

    @NotNull(message = "Debe ingresar una ubicacion")
    private UbicacionDTO ubicacion;

    private List<EspirituSimpleDTO> espiritus;

    public MediumDTO() {
    }

    public MediumDTO(Long id, String nombre, int mana, UbicacionDTO ubicacion, List<EspirituSimpleDTO> espiritus) {
        this.id = id;
        this.nombre = nombre;
        this.mana = mana;
        this.ubicacion = ubicacion;
        this.espiritus = espiritus;
    }

    public static MediumDTO desdeModelo(Medium medium) {
        List<EspirituSimpleDTO> espiritus =
                medium.getEspiritus() != null ?
                medium.getEspiritus()
                        .stream()
                        .map(EspirituSimpleDTO::desdeModelo)
                        .collect(Collectors.toList()) :
                        List.of();

        return new MediumDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getMana(),
                UbicacionDTO.desdeModelo(medium.getUbicacion()),
                espiritus);

    }

    public Medium aModelo() {
        Medium medium = new Medium(this.nombre, this.mana);
        medium.setId(this.id);
        medium.setUbicacion(this.ubicacion.aModelo());
        medium.setEspiritus(
                this.espiritus != null ?
                        this.espiritus.stream()
                                .map(EspirituSimpleDTO::aModelo)
                                .collect(Collectors.toList()) :
                        List.of()
        );
        return medium;
    }
}
