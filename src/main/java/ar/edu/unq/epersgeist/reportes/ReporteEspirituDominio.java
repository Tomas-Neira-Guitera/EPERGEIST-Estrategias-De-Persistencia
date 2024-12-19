package ar.edu.unq.epersgeist.reportes;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDocument;
import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document
public class ReporteEspirituDominio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    LocalDateTime fecha;

    EspirituDocument dominante;

    EspirituDocument dominado;

    public ReporteEspirituDominio(EspirituDocument dominante, EspirituDocument dominado) {
        this.fecha = LocalDateTime.now();
        this.dominante = dominante;
        this.dominado = dominado;
    }

}
