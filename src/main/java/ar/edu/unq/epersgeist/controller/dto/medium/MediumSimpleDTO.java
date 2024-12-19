package ar.edu.unq.epersgeist.controller.dto.medium;


import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MediumSimpleDTO{

    @NotNull(message = "Debe ingresar un id")
    @NotBlank(message = "El id no puede estar vacio")
    private Long   id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    @NotNull(message = "Debe ingresar una energia")
    @PositiveOrZero(message = "La energia no puede ser negativa")
    private int    mana;

    @NotNull(message = "Debe ingresar una ubicacion")
    @NotBlank(message = "La ubicacion no puede estar vacia")
    private UbicacionDTO ubicacion;

    public MediumSimpleDTO() {
    }

    public MediumSimpleDTO(Long id, String nombre, int mana, UbicacionDTO ubicacion) {
        this.id = id;
        this.nombre = nombre;
        this.mana = mana;
        this.ubicacion = ubicacion;
    }

    public static MediumSimpleDTO desdeModelo(Medium medium){
        return new MediumSimpleDTO(
                medium.getId(),
                medium.getNombre(),
                medium.getMana(),
                UbicacionDTO.desdeModelo(medium.getUbicacion()));
    }

    public Medium aModelo(){
        Medium medium = new Medium(
                this.nombre,
                this.mana,
                this.ubicacion.aModelo()

        );
        medium.setId(this.id);

        return medium;

    }

}
