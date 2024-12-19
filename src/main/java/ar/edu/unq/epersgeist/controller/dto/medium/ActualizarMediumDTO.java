package ar.edu.unq.epersgeist.controller.dto.medium;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarMediumDTO {

    @NotNull(message = "Debe ingresar un id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    public ActualizarMediumDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}



