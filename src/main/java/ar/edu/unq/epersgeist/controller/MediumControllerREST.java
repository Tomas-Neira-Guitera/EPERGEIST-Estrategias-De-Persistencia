package ar.edu.unq.epersgeist.controller;


import ar.edu.unq.epersgeist.controller.dto.espiritu.EspirituDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.ActualizarMediumDTO;
import ar.edu.unq.epersgeist.controller.dto.medium.MediumDTO;
import ar.edu.unq.epersgeist.modelo.medium.Medium;
import ar.edu.unq.epersgeist.servicios.interfaces.MediumService;
import ar.edu.unq.epersgeist.controller.dto.medium.CrearMediumDTO;


import jakarta.validation.Valid;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/medium")
public class MediumControllerREST {
    private final MediumService mediumService;

    public MediumControllerREST(MediumService mediumService) {
        this.mediumService = mediumService;
    }

    @PostMapping
    public ResponseEntity<MediumDTO> crearMedium(@Valid @RequestBody CrearMediumDTO crearMediumDTO) {
        GeoJsonPoint punto = crearMediumDTO.getPunto().aModelo();
        Medium medium = mediumService.crear(crearMediumDTO.aModelo(), punto);
        return ResponseEntity.ok(MediumDTO.desdeModelo(medium));
    }

    @PutMapping
    public void actualizarMedium(@Valid @RequestBody ActualizarMediumDTO mediumAct) {
        mediumService.actualizar(mediumAct);
    }


    @GetMapping("/{id}")
    public ResponseEntity<MediumDTO> recuperarMedium(@PathVariable Long id) {
            Medium mediumToReturn = mediumService.recuperar(id);
            return ResponseEntity.ok(MediumDTO.desdeModelo(mediumToReturn));
    }

    @GetMapping
    public List<MediumDTO> recuperarTodos(){
        return
                mediumService.recuperarTodos().stream()
                .map(MediumDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public void eliminarMedium(@PathVariable Long id) {
            mediumService.eliminar(id);
    }

    @PutMapping("/descansar/{id}")
    public void descansar(@PathVariable Long id) {
            mediumService.descansar(id);
    }

    @PutMapping("/{idMediumExorcista}/exorcizarA/{idMediumAExorcisar}")
    public void exorcizar(@PathVariable Long idMediumExorcista, @PathVariable Long idMediumAExorcisar) {
        mediumService.exorcizar(idMediumExorcista, idMediumAExorcisar);}

    @GetMapping("/espiritus/{id}")
    public List<EspirituDTO> getEspiritus(@PathVariable Long id) {
        return
                mediumService.recuperar(id).getEspiritus()
                .stream()
                .map(EspirituDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @PutMapping ("/{idMedium}/invocarA/{idEspiritu}")
    public void invocar(@PathVariable Long idMedium, @PathVariable Long idEspiritu) {
            mediumService.invocar(idMedium, idEspiritu);
    }

    @PutMapping("/{idMedium}/moverDesde/{latitud}/A/{longitud}")
    public void moverA(@PathVariable Long idMedium , @PathVariable Double latitud, @PathVariable Double longitud) {
        mediumService.mover(idMedium, latitud, longitud);
    }


}
