package ar.edu.unq.epersgeist.utils;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@Document("Snapshots")
public class Snapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private Map<String, Object> sqlData;
    private Map<String, Object> mongoData;
    private Map<String, Object> neoData;
    private LocalDate fecha;

    public Snapshot(Map<String, Object> sqlData, Map<String, Object> mongoData, Map<String, Object> neoData, LocalDate fecha) {
        this.sqlData = sqlData;
        this.mongoData = mongoData;
        this.neoData = neoData;
        this.fecha = fecha;
    }

}
