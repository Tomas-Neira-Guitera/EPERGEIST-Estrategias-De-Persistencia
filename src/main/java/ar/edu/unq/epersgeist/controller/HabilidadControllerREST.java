package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.condicion.CondicionDTO;
import ar.edu.unq.epersgeist.modelo.TipoDeCondicion;

import ar.edu.unq.epersgeist.controller.dto.habilidad.CrearHabilidadDTO;
import ar.edu.unq.epersgeist.controller.dto.habilidad.HabilidadDTO;
import ar.edu.unq.epersgeist.modelo.condicion.Condicion;

import ar.edu.unq.epersgeist.modelo.habilidad.HabilidadNode;

import ar.edu.unq.epersgeist.servicios.interfaces.HabilidadService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/habilidad")
public class HabilidadControllerREST {

    private final HabilidadService habilidadService;

    public HabilidadControllerREST(HabilidadService habilidadService) {
        this.habilidadService = habilidadService;
    }

    @PostMapping
    public ResponseEntity<HabilidadDTO> crearHabilidad(@Valid @RequestBody CrearHabilidadDTO crearHabilidadDTO) {
        HabilidadNode espirituCreado = habilidadService.crear(crearHabilidadDTO.aModelo());
        return ResponseEntity.ok(HabilidadDTO.desdeModelo(espirituCreado));
    }

    @PutMapping("/{nombreHabilidadOrigen}/descubrirHabilidad/{nombreHabilidadDestino}")
    public void descubrirHabilidad(@PathVariable String nombreHabilidadOrigen, @PathVariable String nombreHabilidadDestino, @Valid @RequestBody CondicionDTO condicion){

        Condicion condicionModelo = condicion.aModelo();
        habilidadService.descubrirHabilidad(nombreHabilidadOrigen, nombreHabilidadDestino, condicionModelo);
    }

    @PutMapping("/evolucionarEspiritu/{idEspiritu}")
    public void evolucionar(@PathVariable Long idEspiritu){
        habilidadService.evolucionar(idEspiritu);
    }


    @GetMapping("/{nombre}/conectadas")
    public Set<HabilidadDTO> habilidadesConectadas(@PathVariable String nombre) {
        return habilidadService.habilidadesConectadas(nombre)
                .stream()
                .map(HabilidadDTO::desdeModelo).collect(Collectors.toSet());

    }

    @GetMapping("/habilidadesPosiblesDelEspiritu/{idEspiritu}")
    public Set <HabilidadDTO> habilidadesPosiblesDelEspiritu(@PathVariable Long idEspiritu) {
        Set<HabilidadNode> habilidadesPosibles = habilidadService.habilidadesPosibles(idEspiritu);
        return habilidadesPosibles.stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toSet());
    }

    @PostMapping("/{nombreOrigen}/caminoMasRentable/{nombreDestino}")
    public List<HabilidadDTO> caminoMasRentable(@PathVariable String nombreOrigen, @PathVariable String nombreDestino, @Valid @RequestBody Set<TipoDeCondicion> condiciones) {
        List<HabilidadNode> habilidadesConectadas = habilidadService.caminoMasRentable(nombreOrigen, nombreDestino, condiciones);
        return habilidadesConectadas.stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toList());


    }

    @GetMapping("/caminoMasMutable/{espirituID}/{nombreHabilidad}")
    public List<HabilidadDTO>caminoMasMutable(@PathVariable Long espirituID, @PathVariable String nombreHabilidad) {
        List<HabilidadNode> camino = habilidadService.caminoMasMutable(espirituID, nombreHabilidad);
        return  camino.stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @GetMapping("/caminoMenosMutable/{espirituID}/{nombreHabilidad}")
    public List<HabilidadDTO> caminoMenosMutable(@PathVariable Long espirituID, @PathVariable String nombreHabilidad) {
        List<HabilidadNode> camino = habilidadService.caminoMenosMutable(nombreHabilidad, espirituID);
        return camino.stream()
                .map(HabilidadDTO::desdeModelo)
                .collect(Collectors.toList());
    }

}