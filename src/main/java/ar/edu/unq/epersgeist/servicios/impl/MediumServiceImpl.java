package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.dto.medium.ActualizarMediumDTO;
import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.exceptions.NoExisteUnaUbicacionEnEstePunto;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.AreaDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.CoordenadaDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.CoordenadaFueraDeLosLimitesException;
import ar.edu.unq.epersgeist.servicios.exceptions.MasDe100KilometrosException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;

import ar.edu.unq.epersgeist.servicios.interfaces.HabilidadService;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class MediumServiceImpl implements MediumService {

    private final HabilidadService habilidadService;
    private UbicacionDAO ubicacionDAO;
    private MediumDAO mediumDAO;
    private EspirituDAO espirituDAO;
    private UbicacionService ubicacionService;
    private final AreaDAO areaDAO;
    private CoordenadaDAO coordenadaDAO;

    public MediumServiceImpl(MediumDAO dao, EspirituDAO espirituDAO, UbicacionDAO ubicacionDAO, UbicacionService ubicacionService, HabilidadService habilidadService, AreaDAO areaDAO, CoordenadaDAO coordenadaDAO) {
        this.mediumDAO = dao;
        this.espirituDAO = espirituDAO;
        this.ubicacionDAO = ubicacionDAO;
        this.ubicacionService = ubicacionService;
        this.habilidadService = habilidadService;
        this.areaDAO = areaDAO;
        this.coordenadaDAO = coordenadaDAO;
    }

    @Override
    public void crear(Medium medium) {
            mediumDAO.save(medium);
    }

    @Override
    public Medium crear(Medium medium, Long ubicacionId) {
        Ubicacion ubicacion = ubicacionService.recuperar(ubicacionId);
        medium.setUbicacion(ubicacion);
        return mediumDAO.save(medium);
    }

    @Override
    public Medium crear(Medium medium, GeoJsonPoint punto) {
        if(punto.getX() > 180 || punto.getX() < -180 || punto.getY() > 90 || punto.getY() < -90){
            throw new CoordenadaFueraDeLosLimitesException();
        }

        Area area = areaDAO.encontrarElAreaDelPunto(punto).orElseThrow(NoExisteUnaUbicacionEnEstePunto::new);
        Ubicacion ubicacion = ubicacionService.recuperar(area.getIdUbicacion());
        medium.setUbicacion(ubicacion);
        Medium mediumDB = mediumDAO.save(medium);

        Coordenada coordenada = new Coordenada(punto, mediumDB.getId(), TipoDeEntidad.MEDIUM);

        coordenadaDAO.save(coordenada);
        return mediumDB;

    }

    @Override
    public Medium recuperar(Long id) {
        return mediumDAO.findById(id).orElseThrow(() -> new NoExisteLaEntidadException(Medium.class.getSimpleName(), id));
    }

    @Override
    public List<Medium> recuperarTodos() {
        return mediumDAO.findAll();
    }

    @Override
    public void actualizar(Medium medium) {
        if (!mediumDAO.existsById(medium.getId())) {
            throw new NoExisteLaEntidadException(Medium.class.getSimpleName(), medium.getId());
        }
        mediumDAO.save(medium);
    }

    public void actualizar(ActualizarMediumDTO medium) {
        Medium mediumRecuperado = this.recuperar(medium.getId());
        mediumRecuperado.setNombre(medium.getNombre());

        mediumDAO.save(mediumRecuperado);
    }


    @Override
    public void eliminar(Long id) {
        if (!mediumDAO.existsById(id)) {
            throw new NoExisteLaEntidadException(Medium.class.getSimpleName(), id);
        }
        coordenadaDAO.eliminarCoordenadaDeEntidadConId(id, TipoDeEntidad.MEDIUM);
        mediumDAO.deleteById(id);

    }

    @Override
    public void descansar(Long mediumId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new NoExisteLaEntidadException(Medium.class.getSimpleName(), mediumId));
        medium.descansar();
        mediumDAO.save(medium);
    }

    @Override
    public void exorcizar(Long idMediumExorcista, Long idMediumAExorcizar) {
        Medium mediumExorcista = mediumDAO.findById(idMediumExorcista).orElseThrow(() -> new NoExisteLaEntidadException(Medium.class.getSimpleName(), idMediumExorcista));
        Medium mediumAExorcizar = mediumDAO.findById(idMediumAExorcizar).orElseThrow(() -> new NoExisteLaEntidadException(Medium.class.getSimpleName(), idMediumAExorcizar));

        mediumExorcista.exorcizar(mediumAExorcizar);

        mediumExorcista.getEspiritus().forEach(espiritu -> {habilidadService.evolucionar(espiritu.getId());});
        mediumAExorcizar.getEspiritus().forEach(espiritu -> {habilidadService.evolucionar(espiritu.getId());});

        mediumDAO.save(mediumExorcista);
        mediumDAO.save(mediumAExorcizar);
    }

    @Override
    public List<Espiritu> espiritus(Long id) {
        return mediumDAO.findEspiritusByMediumId(id);
    }

    @Override
    public Espiritu invocar(Long mediumId, Long espirituId) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow(() -> new NoExisteLaEntidadException("Medium", mediumId));
        Espiritu espiritu = espirituDAO.findActiveById(espirituId).orElseThrow(() -> new NoExisteLaEntidadException("Espiritu", espirituId));

        Coordenada coordenadaDelMedium = coordenadaDAO.obtenerCoordenadaDeEntidadConId(mediumId, TipoDeEntidad.MEDIUM).get();
        Coordenada coordenadaDelEspiritu = coordenadaDAO.encontrarEntidadCercana(coordenadaDelMedium.getPunto().getX(), coordenadaDelMedium.getPunto().getY(), espirituId, TipoDeEntidad.ESPIRITU).orElseThrow(MasDe100KilometrosException::new);

        Espiritu espirituInvocado = medium.invocar(espiritu);
        coordenadaDelEspiritu.setPunto(coordenadaDelMedium.getPunto());
        coordenadaDAO.save(coordenadaDelEspiritu);

        mediumDAO.save(medium);
        return espirituInvocado;
    }

    @Override
    public void mover(Long mediumId, Double latitud, Double longitud) {
        Medium medium = mediumDAO.findById(mediumId).orElseThrow( () -> new NoExisteLaEntidadException("Medium", mediumId));

        if(longitud > 180 || longitud < -180 || latitud > 90 || latitud < -90){
            throw new CoordenadaFueraDeLosLimitesException();
        }
        GeoJsonPoint punto = new GeoJsonPoint(longitud, latitud);
        Area area = areaDAO.encontrarElAreaDelPunto(punto).orElseThrow(NoExisteUnaUbicacionEnEstePunto::new);
        Ubicacion ubicacion = ubicacionDAO.findById(area.getIdUbicacion()).orElseThrow( () -> new NoExisteLaEntidadException("Ubicacion", area.getIdUbicacion()));
        medium.mover(ubicacion);
        coordenadaDAO.obtenerCoordenadaDeEntidadConId(mediumId, TipoDeEntidad.MEDIUM).ifPresent(coordenada -> {
            coordenada.setPunto(punto);
            coordenadaDAO.save(coordenada);
        });
        coordenadaDAO.obtenerCoordenadasDeEntidadesConIds(medium.getEspiritus().stream().map(Espiritu::getId).toList(), TipoDeEntidad.ESPIRITU).forEach(coordenada -> {
            coordenada.setPunto(punto);
            coordenadaDAO.save(coordenada);
        });
        mediumDAO.save(medium);
    }
}