package ar.edu.unq.epersgeist.controller.dto.habilidad;

import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class HabilidadDTO {

    @NotNull(message = "Debe ingresar un id")
    private Long id;

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombre;

    public HabilidadDTO() {

    }

    public HabilidadDTO(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public static HabilidadDTO desdeModelo(HabilidadNode habilidadNode) {
        return new HabilidadDTO(
                habilidadNode.getId(),
                habilidadNode.getNombre());
    }

    public HabilidadNode aModelo() {
        return new HabilidadNode(this.id, this.nombre);
    }
}
