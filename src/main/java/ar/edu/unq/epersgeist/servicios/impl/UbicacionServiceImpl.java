package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.ActualizarUbicacionDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.UbicacionDAO;
import ar.edu.unq.epersgeist.persistencia.mongodb.AreaDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.EntidadConNombreYaExistenteException;
import ar.edu.unq.epersgeist.servicios.exceptions.ExisteUnaUbicacionEnEseAreaException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.interfaces.ReportService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class UbicacionServiceImpl implements UbicacionService {

    private final UbicacionDAO ubicacionDAO;
    private final EspirituDAO espirituDAO;
    private final MediumDAO mediumDAO;
    private final AreaDAO areaDAO;
    private final ReportService reportService;


    public UbicacionServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO, MediumDAO mediumDAO, ReportService reportService, AreaDAO areaDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
        this.reportService = reportService;
        this.areaDAO = areaDAO;
    }

    @Override
    public Ubicacion crear(Ubicacion ubicacion, Area area) {
        try {
            areaDAO.encontrarAreaEnPoligono(area.getPoligono()).ifPresent(a -> {
                throw new ExisteUnaUbicacionEnEseAreaException();
            });

            Ubicacion ubicacionRes = ubicacionDAO.save(ubicacion);

            area.setIdUbicacion(ubicacionRes.getId());

            areaDAO.save(area);
            return ubicacionDAO.save(ubicacion) ;
        } catch (DataIntegrityViolationException e) {
            throw new EntidadConNombreYaExistenteException("Ubicacion");
        }
    }

    @Override
    public Ubicacion recuperar(Long id) { return ubicacionDAO.findById(id).orElseThrow( () -> new NoExisteLaEntidadException("Ubicacion", id)) ; }

    @Override
    public List<Ubicacion> recuperarTodos() { return ubicacionDAO.findAll();  }


    @Override
    public void actualizar(Ubicacion ubicacion) {
        if (! ubicacionDAO.existsById(ubicacion.getId())) {
            throw new NoExisteLaEntidadException("Ubicacion", ubicacion.getId());
        }

        if (ubicacionDAO.existsByNombreAndIdNot(ubicacion.getNombre(), ubicacion.getId())){
            throw new EntidadConNombreYaExistenteException("Ubicacion");
        }
        ubicacionDAO.save(ubicacion);
    }

    @Override
    public void actualizar(ActualizarUbicacionDTO dto) {
        Ubicacion ubicacionRecuperada = this.recuperar(dto.getId());

        if (ubicacionDAO.existsByNombreAndIdNot(dto.getNombre(), dto.getId())){
            throw new EntidadConNombreYaExistenteException("Ubicacion");
        }
        ubicacionRecuperada.setEnergia(dto.getEnergia());
        ubicacionRecuperada.setNombre(dto.getNombre());
        this.actualizar(ubicacionRecuperada);
    }

    @Override
    public void eliminar(Long id) {
        if (!ubicacionDAO.existsById(id)) {
            throw new NoExisteLaEntidadException("Ubicacion", id);
        }

        areaDAO.eliminarPorIdDeUbicacion(id);
        ubicacionDAO.deleteById(id);
    }

    @Override
    public List<Espiritu> espiritusEn(Long id) {
        return espirituDAO.espiritusEn(id);
    }

    @Override
    public List<Medium> mediumsSinEspiritusEn(Long id) {
        return mediumDAO.mediumsSinEspiritusEn(id);
    }

}