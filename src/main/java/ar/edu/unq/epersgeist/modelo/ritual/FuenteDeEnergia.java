package ar.edu.unq.epersgeist.modelo.ritual;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Repository
@Getter
@Setter
public class FuenteDeEnergia {

    private List<String> palabrasComunes;
    private List<String> palabrasRaras;
    private List<String> palabrasMisticas;

    public FuenteDeEnergia() {
        this.palabrasComunes = new ArrayList<>(List.of("epers", "costo", "transaccion", "acid", "cloud", "cap", "aws", "dynamo", "angel", "espiritu", "demonio", "spring", "java", "dominio", "medium", "persistencia", "modelo", "tabla"));
        this.palabrasRaras = new ArrayList<>(List.of("jeff", "virginia", "norte", "service", "disponibilidad", "serverless", "jdbc", "jpa", "epersgeist", "cementerio", "lambda", "ses", "bucket", "route", "s3", "json"));    //todo modificar
        this.palabrasMisticas = new ArrayList<>(List.of("guido", "particion", "maguin", "ritual", "xd", "picaro"));
    }

    public void agregarPalabraComun(String palabra) {
        this.palabrasComunes.add(palabra.toLowerCase());
    }

    public void agregarPalabraRara(String palabra) {
        this.palabrasRaras.add(palabra.toLowerCase());
    }

    public void agregarPalabraMistica(String palabra) {
        this.palabrasMisticas.add(palabra.toLowerCase());
    }

    public void cargarRitual(Ritual ritual) {
        if (ritual.getEnergiaRitual() == 0) {
            ritual.getPalabrasDelRitual().forEach(palabra -> {
                if (this.palabrasComunes.contains(palabra)) {
                ritual.setEnergiaRitual(ritual.getEnergiaRitual() + 2);
                } else if (this.palabrasRaras.contains(palabra)) {
                    ritual.setEnergiaRitual(ritual.getEnergiaRitual() + 5);
                } else if (this.palabrasMisticas.contains(palabra)) {
                    ritual.setEnergiaRitual(ritual.getEnergiaRitual() + 10);
                }
            });
        }
    }
}
