package ar.edu.unq.epersgeist.modelo.espiritu;

import ar.edu.unq.epersgeist.modelo.enums.TipoDeEspiritu;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Document
public class EspirituDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Long sqlId;

    private String nombre;

    private int energia;

    private TipoDeEspiritu tipoDeEspiritu;

    private List<EspirituDocument> espiritusDominados;

    private String ubicacion;

    public EspirituDocument(List<EspirituDocument> espiritusDominados, TipoDeEspiritu tipoDeEspiritu, int energia, String nombre, String ubicacion, Long sqlId) {
        this.espiritusDominados = espiritusDominados;
        this.tipoDeEspiritu = tipoDeEspiritu;
        this.energia = energia;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.sqlId = sqlId;
    }

    public static EspirituDocument  desdeModelo(Espiritu espiritu){
        return new EspirituDocument(
                espiritu.getEspiritusDominados().stream().map(EspirituDocument::desdeModelo).toList(),
                espiritu.getTipo(),
                espiritu.getEnergia(),
                espiritu.getNombre(),
                espiritu.getUbicacion().getNombre(),
                espiritu.getId()
        );
    }
}
