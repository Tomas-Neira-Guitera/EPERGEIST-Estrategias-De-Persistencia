package ar.edu.unq.epersgeist.persistencia.jpa;
import ar.edu.unq.epersgeist.modelo.ubicacion.Santuario;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UbicacionDAO extends JpaRepository<Ubicacion,Long> {

        @Query("select s from Ubicacion s join Espiritu e on s.id = e.ubicacion.id where type(s) = Santuario group by s.id having count(case when type(e) = Demonio then 1 end) > count(case when type(e) = Angel then 1 end) order by (count(case when type(e) = Demonio then 1 end) - count(case when type(e) = Angel then 1 end )) desc, s.nombre asc limit 1")
        Santuario getSantuarioMasCorrupto();

        @Query("SELECT COUNT(u) > 0 FROM Ubicacion u WHERE u.nombre = :nombre AND u.id <> :id")
        boolean existsByNombreAndIdNot(String nombre, Long id);

        Optional<Ubicacion> getUbicacionByNombre(String nombre);
}