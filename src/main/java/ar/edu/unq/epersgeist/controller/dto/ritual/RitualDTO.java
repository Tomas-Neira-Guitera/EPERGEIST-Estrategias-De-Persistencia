package ar.edu.unq.epersgeist.controller.dto.ritual;

import ar.edu.unq.epersgeist.modelo.ritual.Ritual;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
public class RitualDTO {

    private String id;

    private String nombre;

    private String medium;

    private List<String> palabrasUsadas;

    private int puntaje;

    public RitualDTO(String nombre, String medium, List<String> palabras, int puntaje, String id) {
        this.nombre = nombre;
        this.medium = medium;
        this.palabrasUsadas = palabras;
        this.puntaje = puntaje;
        this.id = id;
    }

    public static RitualDTO desdeModelo(Ritual ritual) {
        return new RitualDTO(
                ritual.getNombre(),
                ritual.getMedium(),
                ritual.getPalabrasDelRitual(),
                ritual.getEnergiaRitual(),
                ritual.getRitualId());
    }
}
