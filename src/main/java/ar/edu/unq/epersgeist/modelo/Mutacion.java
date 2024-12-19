package ar.edu.unq.epersgeist.modelo;

import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.RelationshipId;
import org.springframework.data.neo4j.core.schema.RelationshipProperties;
import org.springframework.data.neo4j.core.schema.TargetNode;


@NoArgsConstructor
@Getter
@Setter
@RelationshipProperties
public class Mutacion {

    @RelationshipId
    private Long id;

    @Property("tipoDeCondicion")
    private TipoDeCondicion tipoDeCondicion;

    @Property("cantidad")
    @Min(value = 0)
    private int cantidad;

    @TargetNode
    private HabilidadNode habilidadDestino;

    public Mutacion(TipoDeCondicion tipoDeCondicion, int cantidad, HabilidadNode habilidadDestino) {
        this.tipoDeCondicion = tipoDeCondicion;
        this.cantidad = cantidad;
        this.habilidadDestino = habilidadDestino;
    }

}
