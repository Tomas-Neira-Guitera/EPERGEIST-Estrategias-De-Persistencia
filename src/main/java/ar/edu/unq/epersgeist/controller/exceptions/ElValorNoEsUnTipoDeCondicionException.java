package ar.edu.unq.epersgeist.controller.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class ElValorNoEsUnTipoDeCondicionException extends RuntimeException {
    public ElValorNoEsUnTipoDeCondicionException(String message) {
        super(message);
    }
}
