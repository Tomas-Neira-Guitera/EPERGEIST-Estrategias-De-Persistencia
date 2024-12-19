package ar.edu.unq.epersgeist.persistencia.mongodb;

import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDocument;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.reportes.ReporteEspirituDominio;
import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface ReporteEspirituDominioDAO extends MongoRepository<ReporteEspirituDominio, String> {


    @Aggregation(pipeline = {
            "{ '$match': { 'fecha': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$group': { '_id': '$dominante.ubicacion', 'angeles': { '$sum': { '$cond': [{ '$eq': ['$dominante.tipoDeEspiritu', 'ANGEL'] }, 1, 0] } }, 'demonios': { '$sum': { '$cond': [{ '$eq': ['$dominante.tipoDeEspiritu', 'DEMONIO'] }, 1, 0] } } } }",
            "{ '$project': { 'ubicacion': '$_id', '_id': 0, 'angelesSobreDemonios': { '$subtract': ['$angeles', '$demonios'] } } }",
            "{ '$sort': { 'angelesSobreDemonios': -1 } }",
            "{ '$limit': 1 }",
            "{ '$project': { 'ubicacion': 1 } }"
    })
    Optional<String> encontrarUbicacionConMayorDiferenciaDeAngelesDominantesEnRangoDeFechas(LocalDateTime fechaInicio, LocalDateTime fechaFin);

    @Aggregation({
            "{ '$match': { 'fecha': { '$gte': ?0, '$lte': ?1 } } }",
            "{ '$project': { 'dominante': 1, 'energiaReporte': '$dominante.energia', 'espiritusDominadosConEnergiaAlta': { '$filter': { 'input': '$dominante.espiritusDominados', 'as': 'espiritu', 'cond': { '$gt': ['$$espiritu.energia', 40] } } } } }",
            "{ '$group': { '_id': '$dominante.sqlId', 'numeroDeEspiritusDominados': { '$max': { '$size': '$espiritusDominadosConEnergiaAlta' } }, 'maxEnergia': { '$max': '$energiaReporte' } } }",
            "{ '$sort': { 'numeroDeEspiritusDominados': -1, 'maxEnergia': -1 } }",
            "{ '$limit': 1 }"
    })
    Optional<EspirituDocument> encontrarEspirituDominante(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
