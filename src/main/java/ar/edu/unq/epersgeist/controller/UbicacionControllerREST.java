package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.reporte.ReporteSantuarioMasCorruptoDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.ActualizarUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.CrearUbicacionDTO;
import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.ubicacion.Area;
import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.servicios.interfaces.ReportService;
import ar.edu.unq.epersgeist.servicios.interfaces.UbicacionService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ubicacion")
public final class  UbicacionControllerREST {

    private final UbicacionService ubicacionService;
    private final ReportService reportService;

    public UbicacionControllerREST(UbicacionService ubicacionService, ReportService reporteService) {
        this.ubicacionService = ubicacionService;
        this.reportService = reporteService;
    }

    @PostMapping
    public ResponseEntity<UbicacionDTO> crearUbicacion(@Valid @RequestBody CrearUbicacionDTO crearUbicacionDTO){
        Area areaBody = crearUbicacionDTO.getArea().aModelo();
        Ubicacion ubicacionCreada = ubicacionService.crear(crearUbicacionDTO.aModelo(), areaBody);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacionCreada));
    }

    @GetMapping( "/{id}")
    public ResponseEntity<UbicacionDTO> recuperarUbicacion(@PathVariable Long id){
        Ubicacion ubicacion = ubicacionService.recuperar(id);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacion));
    }

    @GetMapping
    public List<UbicacionDTO> recuperarTodas(){
        return  ubicacionService.recuperarTodos()
                .stream()
                .map(UbicacionDTO::desdeModelo).toList();
    }

    @PutMapping
    public void actualizar(@Valid @RequestBody ActualizarUbicacionDTO actualizarDTO){
        ubicacionService.actualizar(actualizarDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        ubicacionService.eliminar(id);
    }

    @GetMapping("/{id}/espiritus")
    public List<EspirituDTO> espiritusEn(@PathVariable Long id) {
        List<EspirituDTO> espiritusDTOs =
                ubicacionService.espiritusEn(id)
                        .stream()
                        .map(EspirituDTO::desdeModelo)
                        .toList();

        return espiritusDTOs;
    }

    @GetMapping("/{id}/mediumsSinEspiritus")
    public List<MediumDTO> mediumsSinEspiritusEn(@PathVariable Long id) {
        List<MediumDTO> mediumDTOs =
                ubicacionService.mediumsSinEspiritusEn(id)
                        .stream()
                        .map(MediumDTO::desdeModelo)
                        .toList();
        return mediumDTOs;
    }

    @GetMapping("/santuarioMasCorrupto")
    public ResponseEntity<ReporteSantuarioMasCorruptoDTO> santuarioMasCorrupto(){
        return ResponseEntity.ok(ReporteSantuarioMasCorruptoDTO.desdeModelo(reportService.santuarioCorrupto()));
    }
}