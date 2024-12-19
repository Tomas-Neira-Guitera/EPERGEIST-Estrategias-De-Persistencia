package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.reportes.ReporteSantuarioMasCorrupto;

public interface ReportService {
    Ubicacion getSantuarioMasCorrupto();
    Medium getMediumEndemoniadoEn(Long ubicacionId);
    int cantidadDeDemoniosEn(Long ubicacionId);
    int cantidadDeDemoniosLibresEn(Long ubicacionId);
    ReporteSantuarioMasCorrupto santuarioCorrupto();
}
