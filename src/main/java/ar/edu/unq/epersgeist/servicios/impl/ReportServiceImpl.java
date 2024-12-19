package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.persistencia.jpa.EspirituDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.MediumDAO;
import ar.edu.unq.epersgeist.persistencia.jpa.UbicacionDAO;
import ar.edu.unq.epersgeist.reportes.ReporteSantuarioMasCorrupto;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteLaEntidadException;
import ar.edu.unq.epersgeist.servicios.exceptions.NoExisteUnSantuarioCorruptoException;
import ar.edu.unq.epersgeist.servicios.interfaces.ReportService;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;


@Service
@Transactional
public class ReportServiceImpl implements ReportService {

    private UbicacionDAO ubicacionDAO;
    private EspirituDAO espirituDAO;
    private MediumDAO mediumDAO;

    public ReportServiceImpl(UbicacionDAO ubicacionDAO, EspirituDAO espirituDAO, MediumDAO mediumDAO) {
        this.ubicacionDAO = ubicacionDAO;
        this.espirituDAO = espirituDAO;
        this.mediumDAO = mediumDAO;
    }

    @Override
    public Ubicacion getSantuarioMasCorrupto() {
        Ubicacion santuarioMasCorrupto = ubicacionDAO.getSantuarioMasCorrupto();
        if (santuarioMasCorrupto == null) {
            throw new NoExisteUnSantuarioCorruptoException("No existe un santuario corrupto");
        }
        else {
            return santuarioMasCorrupto;
        }

    }

    @Override
    public Medium getMediumEndemoniadoEn(Long ubicacionId){
        return mediumDAO.getMediumEndemoniadoEn(ubicacionId);
    }

    @Override
    public int cantidadDeDemoniosEn(Long ubicacionId){
        if (ubicacionDAO.existsById(ubicacionId)) {
            return espirituDAO.cantidadDeDemoniosEn(ubicacionId);
        }
        else {
            throw new NoExisteLaEntidadException(Ubicacion.class.getSimpleName(), ubicacionId);
        }
    }

    @Override
    public int cantidadDeDemoniosLibresEn(Long ubicacionId){
        if (ubicacionDAO.existsById(ubicacionId)) {
            return espirituDAO.cantidadDeDemoniosLibresEn(ubicacionId);
        }
        else {
            throw new NoExisteLaEntidadException(Ubicacion.class.getSimpleName(), ubicacionId);
        }
    }

    @Override
    public ReporteSantuarioMasCorrupto santuarioCorrupto() {
        Ubicacion santuarioMasCorrupto = this.getSantuarioMasCorrupto();
        Medium mediumMasCorrupto = this.getMediumEndemoniadoEn(santuarioMasCorrupto.getId());
        int cantidadDeDemoniosTotales = this.cantidadDeDemoniosEn(santuarioMasCorrupto.getId());
        int cantidadDeDemoniosLibres = this.cantidadDeDemoniosLibresEn(santuarioMasCorrupto.getId());

        return new ReporteSantuarioMasCorrupto(santuarioMasCorrupto.getNombre(), mediumMasCorrupto, cantidadDeDemoniosTotales, cantidadDeDemoniosLibres);
    }

}