package ar.edu.unq.epersgeist.controller;

import ar.edu.unq.epersgeist.controller.exceptions.ElValorNoEsUnTipoDeCondicionException;
import ar.edu.unq.epersgeist.controller.exceptions.PoligonoIncompletoException;
import ar.edu.unq.epersgeist.modelo.exceptions.ModeloException;
import ar.edu.unq.epersgeist.modelo.exceptions.NoExisteUnaUbicacionEnEstePunto;
import ar.edu.unq.epersgeist.servicios.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingPathVariableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ExceptionControllerHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        return ResponseEntity.badRequest().body(errors);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Map<String, String>> handleTypeMismatch(MethodArgumentTypeMismatchException e) {
        return buildResponse("Formato de argumento incorrecto: " + e.getName(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMessageNotReadable(HttpMessageNotReadableException e) {
        return buildResponse("El body dado tiene un formato incorrecto", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ModeloException.class)
    public ResponseEntity<Map<String, String>> handleModeloException(ModeloException ex, WebRequest request){
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingPathVariableException.class)
    public ResponseEntity<Map<String, String>> handleMissingPathVariable(Exception ex, WebRequest request) {
        return buildResponse("Falta una variable de ruta en la solicitud", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<Map<String, String>> handleMissingServletRequestParameter(Exception ex, WebRequest request){
        return buildResponse("Falta un parámetro en la solicitud", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        return buildResponse("Error de integridad de los datos enviados", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoExisteLaEntidadException.class)
    public ResponseEntity<Map<String, String>> handleEntityNotFoundException(NoExisteLaEntidadException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EntidadConNombreYaExistenteException.class)
    public ResponseEntity<Map<String, String>> handleEntityAlreadyExistsException(EntidadConNombreYaExistenteException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(NoExisteUnMediumEndemoniadoException.class)
    public ResponseEntity<Map<String, String>> handleNoExisteUnMediumEndemoniadoException(NoExisteUnMediumEndemoniadoException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NoExisteUnSantuarioCorruptoException.class)
    public ResponseEntity<Map<String, String>> handleNoExisteUnSantuarioCorruptoException(NoExisteUnSantuarioCorruptoException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<Map<String, String>> handleHttpMediaTypeNotAcceptableException(Exception ex, WebRequest request) {
        return buildResponse("La creación del JSON no se está realizando correctamente", HttpStatus.NOT_ACCEPTABLE);
    }

    @ExceptionHandler(NoExisteLaHabilidadException.class)
    public ResponseEntity<Map<String, String>> handleNoExisteLaHabilidadException(NoExisteLaHabilidadException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MutacionImposibleException.class)
    public ResponseEntity<Map<String, String>> handleMutacionImposibleException(MutacionImposibleException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ElValorNoEsUnTipoDeCondicionException.class)
    public ResponseEntity<Map<String, String>> handleElValorNoEsUnTipoDeCondicionException(ElValorNoEsUnTipoDeCondicionException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HabilidadesNoConectadasException.class)
    public ResponseEntity<Map<String, String>> handleHabilidadesNoConectadasException(HabilidadesNoConectadasException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundEndpoint(Exception ex, WebRequest request) {
        return buildResponse("El endpoint solicitado no existe", HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CoordenadaFueraDeLosLimitesException.class)
    public ResponseEntity<Map<String, String>> handleCoordenadaFueraDeLosLimitesException(CoordenadaFueraDeLosLimitesException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ExisteUnaUbicacionEnEseAreaException.class)
    public ResponseEntity<Map<String, String>> handleExisteUnaUbicacionEnEseAreaException(ExisteUnaUbicacionEnEseAreaException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoExisteUnaUbicacionEnEstePunto.class)
    public ResponseEntity<Map<String, String>> handleNoExisteUnaUbicacionEnEstePunto(NoExisteUnaUbicacionEnEstePunto ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PoligonoIncompletoException.class)
    public ResponseEntity<Map<String, String>> handlePoligonoIncompletoException(PoligonoIncompletoException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InformacionNoDisponibleException.class)
    public ResponseEntity<Map<String, String>> handleNoExisteUnaEstadisticaDisponible(InformacionNoDisponibleException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoSeEncontroSnapshotException.class)
    public ResponseEntity<Map<String, String>> handleNoSeEncontroSnapshotException(NoSeEncontroSnapshotException ex, WebRequest request) {
        return buildResponse(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleGlobalException(Exception ex, WebRequest request) {
        return buildResponse("Internal server error", HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Map<String, String>> buildResponse(String message, HttpStatus status) {
        Map<String, String> body = new HashMap<>();
        body.put("message", message);
        return new ResponseEntity<>(body, status);
    }


}
