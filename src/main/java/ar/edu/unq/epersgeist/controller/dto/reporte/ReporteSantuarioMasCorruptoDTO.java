package ar.edu.unq.epersgeist.controller.dto.reporte;

import ar.edu.unq.epersgeist.controller.dto.medium.MediumSimpleDTO;
import ar.edu.unq.epersgeist.reportes.ReporteSantuarioMasCorrupto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReporteSantuarioMasCorruptoDTO {

    @NotBlank(message = "El nombre no puede estar vacio")
    @NotNull(message = "Debe ingresar un nombre")
    private String nombreSantuario;

    @NotNull(message = "Debe ingresar un medium")
    private MediumSimpleDTO mediumSimpleDTO;

    @NotNull
    @PositiveOrZero
    private int demoniosTotales;

    @NotNull
    @PositiveOrZero
    private int demoniosLibres;

    public ReporteSantuarioMasCorruptoDTO(String nombreSantuario, MediumSimpleDTO mediumSimpleDTO, int demoniosTotales, int demoniosLibres) {
        this.nombreSantuario = nombreSantuario;
        this.mediumSimpleDTO = mediumSimpleDTO;
        this.demoniosTotales = demoniosTotales;
        this.demoniosLibres = demoniosLibres;
    }

    public static ReporteSantuarioMasCorruptoDTO desdeModelo(ReporteSantuarioMasCorrupto reporte) {
        return new ReporteSantuarioMasCorruptoDTO(reporte.getNombreSantuario(), MediumSimpleDTO.desdeModelo(reporte.getMedium()), reporte.getDemoniosTotales(), reporte.getDemoniosLibres());
    }

}
