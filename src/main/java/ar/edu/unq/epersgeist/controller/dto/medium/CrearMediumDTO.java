package ar.edu.unq.epersgeist.controller.dto.medium;



import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearPuntoDTO;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class CrearMediumDTO {
    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String       nombre;


    @NotNull(message = "Debe ingresar una mana")
    @PositiveOrZero(message = "El mana no puede ser negativo")
    private int          mana;


    @NotNull(message = "Debe ingresar un punto de una ubicacion valido")
    private CrearPuntoDTO punto;

    public CrearMediumDTO() {
    }

    public CrearMediumDTO(String nombre, int mana, CrearPuntoDTO punto) {
        this.nombre = nombre;
        this.mana = mana;
        this.punto = punto;
    }

    public static CrearMediumDTO desdeModelo(Medium medium, CrearPuntoDTO punto) {
        return new CrearMediumDTO(
                medium.getNombre(),
                medium.getMana(),
                punto
        );
    }

    public Medium aModelo(){
       return new Medium(
                this.nombre,
                this.mana
        );

    }
}



