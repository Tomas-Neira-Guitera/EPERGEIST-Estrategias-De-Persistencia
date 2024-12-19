package ar.edu.unq.epersgeist.controller;



import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;

import ar.edu.unq.epersgeist.controller.dto.ubicacion.UbicacionDTO;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

import ar.edu.unq.epersgeist.modelo.ubicacion.Ubicacion;
import ar.edu.unq.epersgeist.controller.dto.estadistica.EstadisticaDTO;

import ar.edu.unq.epersgeist.servicios.interfaces.EstadisticaService;


import org.springframework.format.annotation.DateTimeFormat;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


import java.time.LocalDateTime;


@RestController
@CrossOrigin
@RequestMapping("/estadistica")
public class EstadisticaControllerREST {
    private final EstadisticaService estadisticaService;

    public EstadisticaControllerREST(EstadisticaService estadisticaService) {
        this.estadisticaService = estadisticaService;
    }

    @GetMapping( "/deUbicacionEntre/{fechaInicio}/y/{fechaFin}")
    public ResponseEntity<UbicacionDTO> ubicacionMasDominada(
            @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime fechaInicio,
            @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime fechaFin) {
        Ubicacion ubicacion = estadisticaService.ubicacionMasDominada(fechaInicio, fechaFin);
        return ResponseEntity.ok(UbicacionDTO.desdeModelo(ubicacion));
    }

    @GetMapping("/deEspirituEntre/{fechaInicio}/y/{fechaFin}")
    public ResponseEntity<EspirituDTO> espirituMasDominante(
            @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime fechaInicio,
            @PathVariable @DateTimeFormat(pattern = "dd-MM-yyyy HH:mm:ss") LocalDateTime fechaFin) {

        Espiritu espiritu = estadisticaService.espirituMasDominante(fechaInicio, fechaFin);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espiritu));
    }

    @PostMapping
    public void crearSnapshot() {
        estadisticaService.crearSnapshot();
    }

    @GetMapping("/{fecha}")
    public ResponseEntity<EstadisticaDTO> obtenerSnapshot(@PathVariable LocalDate fecha) {
        return ResponseEntity.ok(EstadisticaDTO.desdeModelo(estadisticaService.obtenerSnapshot(fecha)));
    }
}