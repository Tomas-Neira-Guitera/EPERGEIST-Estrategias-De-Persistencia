package ar.edu.unq.epersgeist.controller.dto.habilidad;

import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrearHabilidadDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    public CrearHabilidadDTO() {

    }

    public CrearHabilidadDTO(String nombre) {
        this.nombre = nombre;
    }

    public static CrearHabilidadDTO desdeModelo(HabilidadNode habilidadNode) {
        return new CrearHabilidadDTO(habilidadNode.getNombre());
    }

    public HabilidadNode aModelo() {
        return new HabilidadNode(this.nombre);
    }
}
