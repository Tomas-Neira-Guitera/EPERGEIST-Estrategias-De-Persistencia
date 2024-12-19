package ar.edu.unq.epersgeist.persistencia.mongodb;

import ar.edu.unq.epersgeist.utils.Snapshot;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface SnapshotDAO extends MongoRepository<Snapshot, String> {

    @Query("{ 'fecha' : ?0 }")
    Optional<Snapshot> obtenerSnapshotPorFecha(LocalDate fecha);
}