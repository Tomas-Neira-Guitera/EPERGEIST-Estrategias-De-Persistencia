package ar.edu.unq.epersgeist.modelo.exceptions;

import ar.edu.unq.epersgeist.modelo.espiritu.Espiritu;

public class EspirituConectadoException extends ModeloException {

    public EspirituConectadoException(Espiritu espiritu) {
        super("El espiritu " + espiritu.getNombre() + "ya se encuentra conectado a un medium");
    }
}
