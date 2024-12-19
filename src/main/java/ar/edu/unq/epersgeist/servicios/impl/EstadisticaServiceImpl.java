package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituSimpleDTO;
import ar.edu.unq.epersgeist.controller.dto.habilidad.HabilidadDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumSimpleDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearAreaDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearPuntoDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDocument;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.mongodb.AreaDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.CoordenadaDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.SnapshotDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.NoSeEncontroSnapshotException;
import ar.edu.unq.epersgeist.servicios.interfaces.*;
import ar.edu.unq.epersgeist.utils.Snapshot;
import jakarta.transaction.Transactional;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import ar.edu.unq.epersgeist.persistencia.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.ReporteEspirituDominioDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.InformacionNoDisponibleException;
import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

import java.time.LocalDateTime;

@Service
@Transactional
public class EstadisticaServiceImpl implements EstadisticaService {

    private EspirituService espirituService;
    private MediumService mediumService;
    private UbicacionService ubicacionService;
    private HabilidadService habilidadService;
    private AreaDAO areaDAO;
    private CoordenadaDAO coordenadaDAO;
    private SnapshotDAO snapshotDAO;

    private ReporteEspirituDominioDAO reporteEspirituDominioDAO;
    private UbicacionDAO ubicacionDAO;
    private EspirituDAO espirituDAO;

    public EstadisticaServiceImpl(EspirituService espirituService,
                                  MediumService mediumService,
                                  UbicacionService ubicacionService,
                                  HabilidadService habilidadService,
                                  AreaDAO areaDAO,
                                  CoordenadaDAO coordenadaDAO,
                                  SnapshotDAO snapshotDAO,
                                  ReporteEspirituDominioDAO reporteEspirituDominioDAO,
                                  UbicacionDAO ubicacionDAO,
                                  EspirituDAO espirituDAO) {
        this.espirituService = espirituService;
        this.mediumService = mediumService;
        this.ubicacionService = ubicacionService;
        this.habilidadService = habilidadService;
        this.areaDAO = areaDAO;
        this.coordenadaDAO = coordenadaDAO;
        this.snapshotDAO = snapshotDAO;
        this.reporteEspirituDominioDAO = reporteEspirituDominioDAO;
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
    }

    @Override
    public Ubicacion ubicacionMasDominada(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
         String nombreUbi = reporteEspirituDominioDAO.encontrarUbicacionConMayorDiferenciaDeAngelesDominantesEnRangoDeFechas(fechaInicio, fechaFin).orElseThrow(InformacionNoDisponibleException::new);
        return ubicacionDAO.getUbicacionByNombre(nombreUbi).orElseThrow(InformacionNoDisponibleException::new);
    }

    @Override
    public Espiritu espirituMasDominante(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        EspirituDocument espirituDocument = reporteEspirituDominioDAO.encontrarEspirituDominante(fechaInicio, fechaFin).orElseThrow(InformacionNoDisponibleException::new);
        return espirituDAO.findActiveById(Long.parseLong(espirituDocument.getId())).orElseThrow(InformacionNoDisponibleException::new);
    }
    @Override
    public void crearSnapshot() {

        List<EspirituSimpleDTO> espiritusSQL = espirituService.recuperarTodos().stream().map(EspirituSimpleDTO::desdeModelo).toList();
        List<MediumSimpleDTO> mediumsSQL = mediumService.recuperarTodos().stream().map(MediumSimpleDTO::desdeModelo).toList();
        List<UbicacionDTO> ubicacionesSQL  = ubicacionService.recuperarTodos().stream().map(UbicacionDTO::desdeModelo).toList();

        List<HabilidadDTO> habilidadesNeo = habilidadService.recuperarTodos().stream().map(HabilidadDTO::desdeModelo).toList();
        List<CrearAreaDTO> areasMongo = areaDAO.findAll().stream()
                .map(area -> CrearAreaDTO.desdeModelo(area.getPoligono().getPoints().stream()
                        .map(punto -> CrearPuntoDTO.desdeModelo((GeoJsonPoint) punto))
                        .toList()))
                .toList();
        List<CrearPuntoDTO> coordenadasMongo = coordenadaDAO.findAll().stream()
                .map(coodenada -> CrearPuntoDTO.desdeModelo(coodenada.getPunto()))
                .toList();

        Map<String, Object> sqlData = new HashMap<>();
        sqlData.put("espiritus", espiritusSQL);
        sqlData.put("mediums", mediumsSQL);
        sqlData.put("ubicaciones", ubicacionesSQL);

        Map<String, Object> mongoData = new HashMap<>();
        mongoData.put("areas", areasMongo);
        mongoData.put("coordenadas", coordenadasMongo);

        Map<String, Object> neoData = new HashMap<>();
        neoData.put("habilidades", habilidadesNeo);

        Snapshot snapshot = new Snapshot(sqlData, mongoData, neoData, LocalDate.now());
        snapshotDAO.save(snapshot);
    }

    @Override
    public Snapshot obtenerSnapshot(LocalDate fecha) {
        Optional<Snapshot> snapshotObtenido = snapshotDAO.obtenerSnapshotPorFecha(fecha);
        if(snapshotObtenido.isEmpty()) {
            throw new NoSeEncontroSnapshotException(fecha);
        }
        return snapshotObtenido.get();
    }
}
