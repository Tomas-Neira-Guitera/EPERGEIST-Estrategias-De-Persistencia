package ar.edu.unq.epersgeist.reportes;

import ar.edu.unq.epersgeist.modelo.medium.Medium;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReporteSantuarioMasCorrupto {

    private String nombreSantuario;
    private Medium medium;
    private int demoniosTotales;
    private int demoniosLibres;

    public ReporteSantuarioMasCorrupto(String nombreSantuario, Medium medium, int demoniosTotales, int demoniosLibres) {
        this.nombreSantuario = nombreSantuario;
        this.medium = medium;
        this.demoniosTotales = demoniosTotales;
        this.demoniosLibres = demoniosLibres;
    }
}
