package ar.edu.unq.epersgeist.modelo.medium;

import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exceptions.*;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import lombok.*;


import java.util.*;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter @Setter
@Entity
public class Medium {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private Integer mana;

    @OneToMany(mappedBy = "medium", cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Espiritu> espiritus;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Ubicacion ubicacion;

    public Medium(@NonNull String nombre, @NonNull Integer mana, @NonNull Ubicacion ubicacion) {
        setMana(mana);
        setUbicacion(ubicacion);
        setEspiritus(new ArrayList<Espiritu>());

        if(nombre.isBlank()){
            throw new EntidadModeloConNombreNullException();
        }
        setNombre(nombre);

    }

    public Medium(@NonNull String nombre, @NonNull Integer mana) {
        setMana(mana);
        setEspiritus(new ArrayList<Espiritu>());

        if(nombre.isBlank()){
            throw new EntidadModeloConNombreNullException();
        }
        setNombre(nombre);

    }


    public void setMana(Integer mana) {
        if (mana < 0) {
            throw new ManaNegativoException();
        }
        this.mana = mana;
    }
    public void crearConexion(Espiritu espiritu) {
        if (Objects.equals(espiritu.getUbicacion().getNombre(), ubicacion.getNombre())) {
            espiritu.conectarseAMedium(this);
            espiritus.add(espiritu);
        } else {
            throw new EspirituNoEstaEnLaMismaUbicacionException(espiritu);
        }

    }
    public void exorcizar(Medium medium) {

        List<Angel> espiritusAngeles = getAngeles();

        if (espiritusAngeles.isEmpty()) {
            throw new ExorcistaSinAngelesException(this);
        }

        List<Demonio> demonios = medium.getDemonios();


        if (demonios.isEmpty()) {
            return;
        }

        for (Angel espirituAngel : espiritusAngeles) {

            if (!demonios.isEmpty()) {
                Demonio demonioAExorcizar = demonios.stream().findFirst().get();

                espirituAngel.atacarA(demonioAExorcizar);

                if (demonioAExorcizar.estaLibre()) {
                    demonios.remove(demonioAExorcizar);
                }
            } else {
                break;
            }
        }
        this.verificarExorcismoExitoso(medium);
    }

    private void verificarExorcismoExitoso(Medium medium) {
        if(medium.getDemonios().isEmpty()){
            this.exorcismoResuelto();
        } else {
           medium.exorcismoEvitado();
        }
    }

    private void exorcismoResuelto() {
        this.getAngeles().forEach(Angel::exorcismoResuelto);
    }

    private void exorcismoEvitado() {
        this.getDemonios().forEach(Demonio::exorcismoEvitado);
    }

    public List<Angel> getAngeles() {
        return espiritus.stream()
                .filter(e -> e.esAngel())
                .map(e -> (Angel) e)
                .collect(Collectors.toList());
    }

    public List<Demonio> getDemonios() {
        return espiritus.stream()
                .filter(e -> e.esDemonio())
                .map(e -> (Demonio) e)
                .collect(Collectors.toList());
    }

    public void desconectarseDeEspiritu(Espiritu espiritu) {
        espiritus.remove(espiritu);
    }
    public void descansar() {
        setMana(getMana() + ubicacion.getManaMedium());
        for (Espiritu espiritu : espiritus) {
            espiritu.aumentarEnergia(ubicacion.getManaEspiritu(espiritu));
        }
    }
    public Espiritu invocar(Espiritu espiritu) {
        if (getMana() > 10)  {
            if (espiritu.estaLibre()) {
                espiritu.invocarseEn(getUbicacion());
                setMana(getMana() - 10);
            } else {
                throw new EspirituConectadoException(espiritu);
            }
        }
        return espiritu;
    }

    public void mover(Ubicacion ubicacion) {
        for (Espiritu espiritu : espiritus) {
            espiritu.mover(ubicacion);
        }

        this.ubicacion = ubicacion;
    }

}
