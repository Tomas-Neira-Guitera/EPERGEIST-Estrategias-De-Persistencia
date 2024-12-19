package ar.edu.unq.epersgeist.modelo.ubicacion;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
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
@DiscriminatorValue("SANTUARIO")
public class Santuario extends Ubicacion{

    public Santuario(String nombre, Integer energia) {
        super(nombre, energia);
    }

    public Santuario(String nombre, Integer energia, Long id) {
        super(nombre, energia, id);
    }

    @Override
    public void validarEspiritu(Espiritu espiritu) {
        if (espiritu.esDemonio()) {
            throw new InvocacionDeDemonioEnSantuarioException();
        }
    }

    @Override
    public Integer getManaMedium() {
        return (int) (this.getEnergia() * 1.5) ;
    }

    @Override
    public int getManaEspiritu(Espiritu espiritu){
        return espiritu.esAngel() ? this.getEnergia() : 0;
    }

    @Override
    public boolean esSantuario(){
        return true;
    }

}
