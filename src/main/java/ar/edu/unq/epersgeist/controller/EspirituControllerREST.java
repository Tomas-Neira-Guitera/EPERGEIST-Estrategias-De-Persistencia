package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituBody;
import ar.edu.unq.epersgeist.controller.dto.espiritu.ActualizarEspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;
import ar.edu.unq.epersgeist.servicios.interfaces.EspirituService;
import ar.edu.unq.epersgeist.utils.Direccion;
import jakarta.validation.Valid;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping("/espiritu")
final public class EspirituControllerREST {

    private EspirituService espirituService;

    public EspirituControllerREST(EspirituService espirituService) {
        this.espirituService = espirituService;
    }

    @GetMapping()
    public List<EspirituDTO> recuperarTodos() {
        return espirituService.recuperarTodos().stream()
                .map(EspirituDTO::desdeModelo)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<EspirituDTO> recuperarEspiritu(@PathVariable  Long id) {

        Espiritu espirituRecuperado = espirituService.recuperar(id);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espirituRecuperado));

    }

    @GetMapping("/demoniacos")
    public List<EspirituDTO> espiritusDemoniacos(@RequestParam Direccion direccion, @RequestParam int pagina, @RequestParam int cantidadPorPagina) {
        return espirituService.espiritusDemoniacos(direccion, pagina, cantidadPorPagina).stream()
                .map(EspirituDTO::desdeModelo)
                .toList();
    }

    @PostMapping
    public ResponseEntity<EspirituDTO> crearEspiritu(@Valid @RequestBody EspirituBody espirituBody) {
        GeoJsonPoint puntoCliente = espirituBody.getPunto().aModelo();
        Espiritu espirituCreado = espirituService.crear(espirituBody.aModelo(), puntoCliente);
        return ResponseEntity.ok(EspirituDTO.desdeModelo(espirituCreado));
    }

    @PutMapping("{espirituId}/conectar/{mediumId}")
    public ResponseEntity<MediumDTO> conectar(@PathVariable Long espirituId, @PathVariable Long mediumId) {

            Medium mediumConectado = espirituService.conectar(espirituId, mediumId);
            return ResponseEntity.ok(MediumDTO.desdeModelo(mediumConectado));

    }

    @PutMapping
    public void actualizarEspiritu(@Valid @RequestBody ActualizarEspirituDTO actualizarEspirituDTO) {
           espirituService.actualizar(actualizarEspirituDTO);
    }

    @DeleteMapping("/{id}")
    public void eliminarEspiritu(@PathVariable Long id) {
        espirituService.eliminar(id);
    }


    @PutMapping("{espirituId}/dominar/{espirituDominadoId}")
    public void dominar(@PathVariable Long espirituId, @PathVariable Long espirituDominadoId) {
        espirituService.dominar(espirituId, espirituDominadoId);
    }


}