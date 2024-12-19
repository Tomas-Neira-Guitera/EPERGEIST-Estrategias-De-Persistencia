package ar.edu.unq.epersgeist.servicios.interfaces;

import ar.edu.unq.epersgeist.modelo.ritual.Ritual;

import java.util.List;

public interface RitualService {
    void crear(String nombre, String medium, String palabras);
    void crear(String nombre, String medium, String palabras, String id);
    Ritual recuperar(String id);
    List<Ritual> recuperarTodos();
    void eliminar(String id);
    void actualizar(String id, String nombre);
    Ritual obtenerElRitualMasPoderoso();
}
