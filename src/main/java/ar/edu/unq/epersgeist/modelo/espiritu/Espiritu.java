package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEspiritu;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import ar.edu.unq.epersgeist.modelo.exceptions.*;


import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;



@NoArgsConstructor
@EqualsAndHashCode
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_de_espiritu", discriminatorType = DiscriminatorType.STRING)
public abstract class Espiritu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 100)
    Integer nivelDeConexion;

    @Column(nullable = false)
    @Min(value = 0)
    @Max(value = 100)
    Integer energia;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private Ubicacion ubicacion;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    protected Medium medium;

    private LocalDateTime deletedAt  = null;

    private int exorcismosResueltos;
    private int exorcismosEvitados;

    @OneToMany(mappedBy = "dominador", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Espiritu> espiritusDominados = new ArrayList<Espiritu>();

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private Espiritu dominador;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    private List<Habilidad> habilidades;

    public Espiritu(@NonNull String nombre, @NonNull Ubicacion  ubicacion, @NonNull Integer energia) {
        this.nivelDeConexion = 0;
        this.ubicacion = ubicacion;
        if (energia < 0 || energia > 100) {
            throw new EnergiaFueraDeLosLimitesException();
        }
        this.energia = energia;

        if(nombre.isBlank()){
            throw new EntidadModeloConNombreNullException();
        }
        this.nombre = nombre;

        this.habilidades = new ArrayList<Habilidad>();
    }

    public Espiritu(@NonNull String nombre, @NonNull Ubicacion  ubicacion, @NonNull Integer energia, Habilidad habilidad) {
        this.nivelDeConexion = 0;
        this.ubicacion = ubicacion;
        if (energia < 0 || energia > 100) {
            throw new EnergiaFueraDeLosLimitesException();
        }
        this.energia = energia;

        if(nombre.isBlank()){
            throw new EntidadModeloConNombreNullException();
        }
        this.nombre = nombre;

        this.habilidades = new ArrayList<Habilidad>();
        this.habilidades.add(habilidad);
    }

    public Espiritu(@NonNull String nombre, @NonNull Integer energia, Habilidad habilidad) {
        this.nivelDeConexion = 0;
        if (energia < 0 || energia > 100) {
            throw new EnergiaFueraDeLosLimitesException();
        }
        this.energia = energia;

        if(nombre.isBlank()){
            throw new EntidadModeloConNombreNullException();
        }
        this.nombre = nombre;

        this.habilidades = new ArrayList<Habilidad>();
        this.habilidades.add(habilidad);
    }

    public Espiritu(String nombre, int energia, int nivelDeConexion, Ubicacion ubicacion, Medium medium, Long id) {
        this.nivelDeConexion = nivelDeConexion;
        this.ubicacion = ubicacion;
        if (energia < 0 || energia > 100) {
            throw new EnergiaFueraDeLosLimitesException();
        }
        this.energia = energia;

        if(nombre.isBlank()){
            throw new EntidadModeloConNombreNullException();
        }
        this.nombre = nombre;

        this.medium = medium;
        this.id = id;
    }

    public Espiritu(String nombre, int energia){
        this.nivelDeConexion = 0;
        this.nombre = nombre;
        this.energia = energia;
        this.nivelDeConexion = 0;
        this.habilidades = new ArrayList<Habilidad>();
    }

    public Espiritu(Long id, String nombre, Ubicacion ubicacion, Integer energia, Integer nivelDeConexion) {
        this.id = id;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.energia = energia;
        this.nivelDeConexion = nivelDeConexion;
    }

    public boolean estaLibre() {
        return this.medium == null && this.dominador == null;
    }

    public boolean estaConectado(){
        return this.medium != null;
    }

    public boolean estaDominado(){
        return this.dominador != null;
    }

     public void conectarseAMedium(Medium medium) {

        this.verificarQueEstaLibre();

        var nivelDeConexion = (int) Math.floor(medium.getMana() * 0.2);

        this.medium = medium;

        this.nivelDeConexion = Math.min(nivelDeConexion, 100);
    }

    private void verificarQueEstaLibre() {
        if(this.estaDominado()){
            throw new EspirituDominadoException();
        }
        if(this.estaConectado()){
            throw new EspirituConectadoException(this);
        }
    }

    public void disminuirEnergia(int cantidad) {
        if (cantidad < 0) {
            throw new CantidadNegativaException();
        }

        this.energia -= cantidad;


        this.energia = Math.max(energia, 0);

    }

    public void aumentarEnergia(int cantidad) {
        if (cantidad < 0) {
            throw new CantidadNegativaException();
        }
        this.energia = Math.min(energia + cantidad, 100);
    }

    public void invocarseEn(Ubicacion ubicacion){

        ubicacion.validarEspiritu(this);

        this.ubicacion = ubicacion;
    }

    public void mover(Ubicacion ubicacion) {
        this.ubicacion = ubicacion;
    }

    public boolean esDemonio(){
        return false;
    }

    public boolean esAngel(){
        return false;
    }

    public void addHabilidad(Habilidad habilidad) {
        this.habilidades.add(habilidad);
    }

    public void dominar(Espiritu espiritu) {
        verificarQueSePuedeDominar(espiritu);
        espiritu.dominador = this;
        this.espiritusDominados.add(espiritu);
    }

    private void verificarQueSePuedeDominar(Espiritu espiritu) {
        if(espiritu.equals(this.dominador)){
            throw new EspirituNoPuedeDominarASuDominadorException();
        }
        espiritu.verificarQueEstaLibre();
        if(espiritu.getEnergia() >= 50){
            throw new EspirituNoDebilitadoException();
        }
    }

    public List<String> getNombreHabilidades(){
        return this.habilidades.stream().map(Habilidad::getNombre).toList();
    }

    public void addHabilidades(List<Habilidad> habilidades){
        this.habilidades.addAll(habilidades);
    }


    public abstract TipoDeEspiritu getTipo();
}