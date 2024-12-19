package ar.edu.unq.epersgeist.servicios.impl;

import ar.edu.unq.epersgeist.modelo.ritual.FuenteDeEnergia;
import ar.edu.unq.epersgeist.modelo.ritual.Ritual;
import ar.edu.unq.epersgeist.persistencia.dynamo.RitualDAO;
import ar.edu.unq.epersgeist.servicios.exceptions.NoSeEncontroRitualException;
import ar.edu.unq.epersgeist.servicios.interfaces.RitualService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;


@Service
@Transactional
public class RitualServiceImpl implements RitualService {

    final RitualDAO ritualDAO;
    final FuenteDeEnergia fuenteDeEnergia;

    public RitualServiceImpl(RitualDAO ritualDAO, FuenteDeEnergia fuenteDeEnergia) {
        this.ritualDAO = ritualDAO;
        this.fuenteDeEnergia = fuenteDeEnergia;
    }

    public void crear(String nombre, String medium, String palabras) {
        Ritual ritual = new Ritual(nombre, medium, palabras);
        ritualDAO.save(ritual);
    }

    public void crear(String nombre, String medium, String palabras, String id) {
        Ritual ritual = new Ritual(nombre, medium, palabras, id);
        ritualDAO.save(ritual);
    }

    public Ritual recuperar(String id) {
        return ritualDAO.get(id);
    }


    public List<Ritual> recuperarTodos() {
        this.cargarRituales();
        return ritualDAO.getAll();
    }


    public void eliminar(String id) {
        ritualDAO.delete(id);
    }


    public void actualizar(String id, String nombre) {
        Ritual ritual = ritualDAO.get(id);
        ritual.setNombre(nombre);
        ritualDAO.save(ritual);
    }

    public void cargarRituales() {
        List<Ritual> rituales = ritualDAO.getAll();
        rituales.forEach(fuenteDeEnergia::cargarRitual);
        rituales.forEach(ritualDAO::save);
    }

    public Ritual obtenerElRitualMasPoderoso() {
        this.cargarRituales();
        Ritual ritualMasPoderos = ritualDAO.getAll().stream().max(Comparator.comparingInt(Ritual::getEnergiaRitual))
                .orElseThrow(() -> new NoSeEncontroRitualException("No existen rituales en la base de datos."));
        // falta la logica para que el medium del ritual se apodere de los espiritus.
        return ritualMasPoderos;
    }

}
