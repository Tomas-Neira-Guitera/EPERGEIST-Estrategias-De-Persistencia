package ar.edu.unq.epersgeist.modelo.exceptions;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class EspirituNoEstaEnLaMismaUbicacionException extends ModeloException {

    public EspirituNoEstaEnLaMismaUbicacionException(Espiritu espiritu) {
        super("El espiritu " + espiritu.getNombre() + " no se encuentra en la misma ubicacion que el medium");
    }
}
