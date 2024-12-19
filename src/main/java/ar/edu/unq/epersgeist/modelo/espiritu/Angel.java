package ar.edu.unq.epersgeist.modelo.espiritu;


import ar.edu.unq.epersgeist.modelo.enums.TipoDeEspiritu;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.random.Random;
import ar.edu.unq.epersgeist.modelo.random.Randomizer;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.NoArgsConstructor;


@NoArgsConstructor
@Entity
@DiscriminatorValue("ANGEL")
public class Angel extends Espiritu {

    public Angel(String nombre, Ubicacion ubicacion, Integer energia) {
        super(nombre, ubicacion, energia);
    }

    public Angel(String nombre, Ubicacion ubicacion, Integer energia, Habilidad habilidad) {
        super(nombre, ubicacion, energia, habilidad);
    }
    public Angel(String nombre, Integer energia, Habilidad habilidad) {
        super(nombre,energia, habilidad);
    }

    public Angel(String nombre, int energia, int nivelDeConexion, Ubicacion ubicacion, Medium medium, Long id) {
        super(nombre,energia,nivelDeConexion,ubicacion,medium,id);
    }

    public Angel (String nombre, int energia) {
        super(nombre,energia);
    }

    public Angel(Long id, String nombre, Ubicacion ubicacion, Integer energia, Integer nivelDeConexion){
        super(id, nombre, ubicacion, energia, nivelDeConexion);
    }

    private boolean tieneEnergiaSuficienteParaAtacar() {
        return this.energia >= 10;
    }
    public void atacarA(Demonio espiritu) {
        if (this.tieneEnergiaSuficienteParaAtacar()){

            Randomizer randomizer = Random.getInstance().getStrategy();

            long porcentajeDeAtaqueExitoso  = randomizer.getNro() + this.nivelDeConexion + this.puntosPorEspirituDominado();

            if (porcentajeDeAtaqueExitoso > 66) {
                espiritu.disminuirEnergiaPorAtaque(this.nivelDeConexion / 2);
            }

            this.disminuirEnergia(10);
        }
    }

    private long puntosPorEspirituDominado() {
        return this.getEspiritusDominados()
                .stream()
                .filter(espiritu -> espiritu.getHabilidades().size() > 3)
                .count();
    }

    public void invocarseEn(Ubicacion ubicacion){
        ubicacion.validarEspiritu(this);
        this.setUbicacion(ubicacion);
    }

    @Override
    public void mover(Ubicacion ubicacion) {
        if (ubicacion.esCementerio()) {
            disminuirEnergia(5);
        }
        super.mover(ubicacion);
    }

    @Override
    public boolean esAngel() {
        return true;
    }

    @Override
    public TipoDeEspiritu getTipo() {
        return TipoDeEspiritu.ANGEL;
    }

    public void exorcismoResuelto() {
        super.setExorcismosResueltos(super.getExorcismosResueltos() + 1);
    }

    @Override
    public int getExorcismosResueltos() {
        return super.getExorcismosResueltos();
    }

}