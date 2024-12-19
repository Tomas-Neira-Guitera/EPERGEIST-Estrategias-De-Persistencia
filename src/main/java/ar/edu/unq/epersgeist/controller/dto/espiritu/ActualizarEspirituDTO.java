package ar.edu.unq.epersgeist.controller.dto.espiritu;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ActualizarEspirituDTO {

    @NotNull(message = "Debe ingresar un id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;


    public ActualizarEspirituDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}