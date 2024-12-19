package ar.edu.unq.epersgeist.controller.dto.ubicacion;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;

@Getter
public class ActualizarUbicacionDTO {

    @NotNull(message = "Debe ingresar un id")
    private Long id;


    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    @NotNull(message = "Debe ingresar una energia")
    @PositiveOrZero(message = "La energia no puede ser negativa")
    @Max(value = 100, message = "La energia no puede ser mayor a 100")
    private Integer energia;


    public ActualizarUbicacionDTO(Long id, String nombre, Integer energia) {
        this.id = id;
        this.nombre = nombre;
        this.energia = energia;
    }
}
