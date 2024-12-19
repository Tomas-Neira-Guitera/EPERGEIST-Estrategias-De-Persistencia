package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exceptions.InvocacionDeAngelEnCementerioException;
import ar.edu.unq.epersgeist.modelo.exceptions.InvocacionDeDemonioEnSantuarioException;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@DiscriminatorValue("CEMENTERIO")
public class Cementerio extends Ubicacion{

    public Cementerio(String nombre, Integer energia) {
        super(nombre, energia);
    }

    public Cementerio(String nombre, Integer energia, Long id) {
        super(nombre, energia, id);
    }

    @Override
    public void validarEspiritu(Espiritu espiritu) {
        if (espiritu.esAngel()) {
            throw new InvocacionDeAngelEnCementerioException();
        }
    }

    @Override
    public Integer getManaMedium() {
        return (int) (this.getEnergia() * 0.5) ;
    }

    @Override
    public int getManaEspiritu(Espiritu espiritu) {
        return espiritu.esDemonio() ? this.getEnergia() : 0;
    }

    @Override
    public boolean esCementerio() {
        return true;
    }

}
