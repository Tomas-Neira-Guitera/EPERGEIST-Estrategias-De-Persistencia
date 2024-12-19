package ar.edu.unq.epersgeist.persistencia.jpa;


import ar.edu.unq.epersgeist.modelo.habilidad.Habilidad;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;
import java.util.Optional;

@Repository
public interface HabilidadDAO extends JpaRepository<Habilidad, Long> {

    @Query("SELECT h FROM Habilidad h WHERE h.nombre = :nombre")
    Optional<Habilidad> findByName(String nombre);

    @Query("SELECT h FROM Habilidad h WHERE h.id IN :ids")
    List<Habilidad> recuperarPorIds(List<Long> ids);

}
