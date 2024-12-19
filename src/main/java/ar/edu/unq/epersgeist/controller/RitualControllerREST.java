package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.dto.ritual.RitualDTO;
import ar.edu.unq.epersgeist.servicios.interfaces.RitualService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/ritual")
final public class RitualControllerREST {

    private final RitualService ritualService;

    public RitualControllerREST(RitualService ritualService) {
        this.ritualService = ritualService;
    }

    @GetMapping
    public List<RitualDTO> recuperarTodos(){
        return
                ritualService.recuperarTodos().stream()
                .map(RitualDTO::desdeModelo)
                .collect(Collectors.toList());
    }

    @GetMapping("/masPoderoso")
    public RitualDTO ritualMasPoderoso(){
        return RitualDTO.desdeModelo(ritualService.obtenerElRitualMasPoderoso());
    }
}
