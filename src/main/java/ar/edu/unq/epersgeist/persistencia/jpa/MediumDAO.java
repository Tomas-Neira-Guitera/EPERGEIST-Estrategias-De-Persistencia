package ar.edu.unq.epersgeist.persistencia.jpa;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediumDAO extends JpaRepository<Medium, Long> {

    @Query("SELECT e FROM Espiritu e WHERE e.medium.id = :id")
    List<Espiritu> findEspiritusByMediumId(Long id);

    @Query("SELECT m FROM Medium m LEFT JOIN m.espiritus e WHERE m.ubicacion.id = :id AND e IS NULL")
    List<Medium> mediumsSinEspiritusEn(Long id);

    @Query("SELECT m FROM Medium m JOIN m.espiritus e WHERE m.ubicacion.id = :ubicacionId AND TYPE(e) = Demonio GROUP BY m.id ORDER BY COUNT(e) DESC, m.nombre asc limit 1")
    Medium getMediumEndemoniadoEn(Long ubicacionId);

}
