package ar.edu.unq.epersgeist.modelo.ubicacion;



import ar.edu.unq.epersgeist.modelo.espiritu.Angel;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exceptions.EnergiaFueraDeLosLimitesException;
import ar.edu.unq.epersgeist.modelo.exceptions.EntidadModeloConNombreNullException;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo_de_ubicacion", discriminatorType = DiscriminatorType.STRING)
public abstract class Ubicacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String nombre;

    @Min(0)
    @Max(100)
    @Column(nullable = false)
    private Integer energia;

    public Ubicacion(@NonNull String nombre, @NonNull Integer energia) {

        if (nombre.isBlank()) {
            throw new EntidadModeloConNombreNullException();
        }

        if (energia < 0 || energia > 100) {
            throw new EnergiaFueraDeLosLimitesException();
        }

        this.energia = energia;
        this.nombre = nombre;
    }

    public Ubicacion(@NonNull String nombre, @NonNull Integer energia,@NonNull Long id) {
        this(nombre, energia);
        this.id = id;
    }

    public boolean esCementerio(){
        return false;
    }

    public boolean esSantuario(){
        return false;
    }

    public abstract void validarEspiritu(Espiritu espiritu);

    public abstract Integer getManaMedium();

    public abstract int getManaEspiritu(Espiritu espiritu);
}
