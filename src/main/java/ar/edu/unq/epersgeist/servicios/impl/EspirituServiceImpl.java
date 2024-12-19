package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.dto.espiritu.ActualizarEspirituDTO;
import ar.edu.unq.epersgeist.modelo.enums.TipoDeEntidad;
import ar.edu.unq.epersgeist.modelo.espiritu.Demonio;
import ar.edu.unq.epersgeist.modelo.espiritu.EspirituDocument;
import ar.edu.unq.epersgeist.modelo.exceptions.EspiritusDistanciaException;
import ar.edu.unq.epersgeist.modelo.exceptions.NoExisteUnaUbicacionEnEstePunto;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Coordenada;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.jpa.HabilidadDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.AreaDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.CoordenadaDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.ReporteEspirituDominioDAO;
import ar.edu.unq.epersgeist.persistencia.neo.HabilidadNeoDAO;
import ar.edu.unq.epersgeist.reportes.ReporteEspirituDominio;
import ar.edu.unq.epersgeist.reportes.ReporteEspirituDominio;
import ar.edu.unq.epersgeist.servicios.exceptions.CoordenadaFueraDeLosLimitesException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import ar.edu.unq.epersgeist.utils.Direccion;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.persistencia.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.MediumDAO;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.utils.Paginador;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class EspirituServiceImpl implements EspirituService {

    private final AreaDAO areaDAO;
    private EspirituDAO espirituDAO;
    private MediumDAO mediumDAO;
    private UbicacionService ubicacionService;
    private HabilidadNeoDAO habilidadNeoDAO;
    private CoordenadaDAO coordenadaDAO;
    private HabilidadDAO habilidadDAO;
    private ReporteEspirituDominioDAO reporteEspirituDominioDAO;

    public EspirituServiceImpl(EspirituDAO daoE, MediumDAO daoM, UbicacionService ubicacionService,
                               AreaDAO areaDAO, CoordenadaDAO coordenadaDAO, HabilidadDAO habilidadDAO, ReporteEspirituDominioDAO reporteEspirituDominioDAO) {
        this.espirituDAO = daoE;
        this.mediumDAO = daoM;
        this.ubicacionService = ubicacionService;
        this.areaDAO = areaDAO;
        this.coordenadaDAO = coordenadaDAO;
        this.habilidadDAO = habilidadDAO;
        this.reporteEspirituDominioDAO = reporteEspirituDominioDAO;
    }

    @Override
    public Espiritu crear(Espiritu espiritu) {
        return espirituDAO.save(espiritu);
    }

    @Override
    public Espiritu crear(Espiritu espiritu, GeoJsonPoint punto) {

        if(punto.getX() > 180 || punto.getX() < -180 || punto.getY() > 90 || punto.getY() < -90){
            throw new CoordenadaFueraDeLosLimitesException();
        }

        Area area = areaDAO.encontrarElAreaDelPunto(punto).orElseThrow(NoExisteUnaUbicacionEnEstePunto::new);

        Ubicacion ubicacion = ubicacionService.recuperar(area.getIdUbicacion());
        espiritu.setUbicacion(ubicacion);
        Espiritu espirituDb = espirituDAO.save(espiritu);

        Coordenada coordenada = new Coordenada(punto, espirituDb.getId(), TipoDeEntidad.ESPIRITU);

        coordenadaDAO.save(coordenada);
        return espirituDb;
    }

    @Override
    public Espiritu recuperar(Long espirituId) {
        return espirituDAO.findActiveById(espirituId).orElseThrow(() -> new NoExisteLaEntidadException("Espiritu", espirituId));
    }

    @Override
    public List<Espiritu> recuperarTodos() {
        return espirituDAO.findByDeletedAtIsNull();
    }

    @Override
    public void actualizar(Espiritu espiritu) {
        if (!espirituDAO.existsActiveById(espiritu.getId())) {
            throw new NoExisteLaEntidadException("Espiritu", espiritu.getId());
        }
        espirituDAO.save(espiritu);
    }

    @Override
    public void actualizar(ActualizarEspirituDTO espirituActualizado) {
        Espiritu espiritu = this.recuperar(espirituActualizado.getId());
        espiritu.setNombre(espirituActualizado.getNombre());
        espirituDAO.save(espiritu);
    }

    @Override
    public void eliminar(Long espirituId) {
        Espiritu espiritu = this.recuperar(espirituId);
        espiritu.setDeletedAt(LocalDateTime.now());

        if (espiritu.getMedium() != null) {
            Medium medium = espiritu.getMedium();
            medium.getEspiritus().remove(espiritu);
            mediumDAO.save(medium);
        }

        coordenadaDAO.eliminarCoordenadaDeEntidadConId(espirituId, TipoDeEntidad.ESPIRITU);

        espirituDAO.save(espiritu);
    }


    @Override
    public List<Demonio> espiritusDemoniacos(Direccion direccion, int pagina, int cantidadPorPagina) {

        Pageable pageable = Paginador.paginar(direccion, pagina, cantidadPorPagina);
        Page<Demonio> demonios = espirituDAO.findDemonios(pageable);
        return demonios.getContent();
    }

    @Override
    public Medium conectar(Long espirituId, Long mediumId) {
        Espiritu espirituAConectar = this.recuperar(espirituId);
        Medium mediumAConectar = mediumDAO.findById(mediumId).orElseThrow(() -> new NoExisteLaEntidadException("Medium", mediumId));

        mediumAConectar.crearConexion(espirituAConectar);

        espirituDAO.save(espirituAConectar);
        mediumDAO.save(mediumAConectar);
        return mediumAConectar;
    }

    @Override
    public void agregarHabilidad(Long espirituId, Long habilidadId) {
        Espiritu espiritu = this.recuperar(espirituId);
        espiritu.addHabilidad(habilidadDAO.findById(habilidadId).orElseThrow(() -> new NoExisteLaEntidadException("Habilidad", habilidadId)));
        espirituDAO.save(espiritu);
    }

    @Override
    public void dominar(Long espirituDominanteId, Long espirituADominarId){

        Espiritu espirituDominante = this.recuperar(espirituDominanteId);
        Espiritu espirituADominar = this.recuperar(espirituADominarId);

        Coordenada coordenadaDominante = coordenadaDAO.obtenerCoordenadaDeEntidadConId(espirituDominanteId, TipoDeEntidad.ESPIRITU)
                .orElseThrow(() -> new NoExisteLaEntidadException("Coordenada", espirituDominanteId));

        Coordenada coordenadaADominar = coordenadaDAO.obtenerCoordenadaDeEntidadConId(espirituADominarId, TipoDeEntidad.ESPIRITU)
                .orElseThrow(() -> new NoExisteLaEntidadException("Coordenada", espirituADominarId));


        var coordenadaEnRango = coordenadaDAO.encontrarCoordenadasDentroDeRangoConId(coordenadaADominar.getPunto(), coordenadaDominante.getId());

        if(coordenadaEnRango.isEmpty()){
            throw new EspiritusDistanciaException();
        }

        espirituDominante.dominar(espirituADominar);

        espirituDAO.save(espirituDominante);

        ReporteEspirituDominio reporte = new ReporteEspirituDominio(
                EspirituDocument.desdeModelo(espirituDominante),
                EspirituDocument.desdeModelo(espirituADominar)
        );

        reporteEspirituDominioDAO.save(reporte);
    }

}