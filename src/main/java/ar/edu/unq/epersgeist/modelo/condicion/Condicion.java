package ar.edu.unq.epersgeist.modelo.condicion;

import ar.edu.unq.epersgeist.modelo.TipoDeCondicion;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Condicion {

    @NotNull
    @NotBlank
    private TipoDeCondicion tipoDeCondicion;

    @NotNull
    @NotBlank
    @Min(value = 0)
    private int cantidad;


    public Condicion(TipoDeCondicion tipoDeCondicion, int cantidad) {
        this.tipoDeCondicion = tipoDeCondicion;
        this.cantidad = cantidad;

    }

}
