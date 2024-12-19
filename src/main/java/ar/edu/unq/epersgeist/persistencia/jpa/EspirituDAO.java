package ar.edu.unq.epersgeist.persistencia.jpa;

import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;


@Repository
public interface EspirituDAO extends JpaRepository<Espiritu, Long>  {

    List<Espiritu> findByDeletedAtIsNull();

    @Query("SELECT e FROM Espiritu e WHERE e.ubicacion.id = :ubicacionId AND e.deletedAt IS NULL")
    public List<Espiritu> espiritusEn(Long ubicacionId);

    @Query("SELECT e FROM Espiritu e WHERE TYPE(e) = Demonio AND e.deletedAt IS NULL")
    Page<Demonio> findDemonios(Pageable pageable);

    @Query("SELECT count(e) FROM Espiritu e WHERE TYPE(e) = Demonio AND e.ubicacion.id = :ubicacionId AND e.deletedAt IS NULL")
    int cantidadDeDemoniosEn(Long ubicacionId);

    @Query("SELECT count(e) FROM Espiritu e WHERE TYPE(e) = Demonio AND e.ubicacion.id = :ubicacionId AND e.medium IS NULL AND e.deletedAt IS NULL")
    int cantidadDeDemoniosLibresEn(Long ubicacionId);

    @Query("SELECT COUNT(e) > 0 FROM Espiritu e WHERE e.id = :id AND e.deletedAt IS NULL")
    boolean existsActiveById(Long id);

    @Query("SELECT e  FROM Espiritu e WHERE e.id = :id AND e.deletedAt IS NULL")
    Optional<Espiritu> findActiveById(Long id);

}
