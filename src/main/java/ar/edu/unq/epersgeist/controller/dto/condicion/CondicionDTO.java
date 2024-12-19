package ar.edu.unq.epersgeist.controller.dto.condicion;

import ar.edu.unq.epersgeist.modelo.TipoDeCondicion;
import ar.edu.unq.epersgeist.modelo.condicion.Condicion;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CondicionDTO {

    @NotNull(message = "Debe ingresar un tipo valido")
    private TipoDeCondicion tipoDeCondicion;

    @NotNull(message = "La cantidad no puede ser nula")
    @PositiveOrZero(message = "La cantidad no puede ser negativa")
    private int cantidad;

    public CondicionDTO() {
    }

    public CondicionDTO(TipoDeCondicion tipoCondicion, int cantidad) {
        this.tipoDeCondicion = tipoCondicion;
        this.cantidad = cantidad;
    }



    public Condicion aModelo(){
        return new Condicion(
                this.tipoDeCondicion,
                this.cantidad
        );

    }
}
