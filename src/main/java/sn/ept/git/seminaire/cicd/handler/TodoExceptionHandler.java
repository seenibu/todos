package sn.ept.git.seminaire.cicd.handler;

import sn.ept.git.seminaire.cicd.exceptions.ForbiddenException;
import sn.ept.git.seminaire.cicd.exceptions.InvalidException;
import sn.ept.git.seminaire.cicd.exceptions.ItemExistsException;
import sn.ept.git.seminaire.cicd.exceptions.ItemNotFoundException;
import sn.ept.git.seminaire.cicd.models.ErrorModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import java.time.Clock;
import java.time.LocalDateTime;

/**
 *
 * @author ISENE
 */

@RestControllerAdvice
public class TodoExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(value= { ItemNotFoundException.class })
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    protected ResponseEntity<ErrorModel> notFound(
            ItemNotFoundException exception, WebRequest request) {
        return buildResponse(HttpStatus.NOT_FOUND.value(), exception, request);
    }

    @ExceptionHandler(value= { ItemExistsException.class })
    @ResponseStatus(value = HttpStatus.CONFLICT)
    protected ResponseEntity<ErrorModel> conflict(
            ItemExistsException exception, WebRequest request) {
        return buildResponse(HttpStatus.CONFLICT.value(), exception, request);
    }



    @ExceptionHandler(value = {InvalidException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    protected ResponseEntity<ErrorModel> badRequest(
            InvalidException exception, WebRequest request) {
        return buildResponse(HttpStatus.BAD_REQUEST.value(), exception, request);
    }


    @ExceptionHandler(value= {ForbiddenException.class })
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    protected ResponseEntity<ErrorModel> permissionDenied(
            ForbiddenException exception, WebRequest request) {
        return buildResponse(HttpStatus.FORBIDDEN.value(), exception, request);
    }

    @ExceptionHandler(value= { Exception.class ,RuntimeException.class})
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    protected ResponseEntity<ErrorModel> internalError(
            Exception exception, WebRequest request) {
        return buildResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), exception, request);
    }

    @ExceptionHandler(value= {ResponseStatusException.class })
    protected ResponseEntity<ErrorModel> responseStatus(
            ResponseStatusException exception, WebRequest request) {
        return buildResponse(exception.getStatusCode().value(), exception, request);
    }


    private static ResponseEntity<ErrorModel> buildResponse(int status, Exception exception, WebRequest request) {
        ErrorModel message =  ErrorModel.builder()
                .status(status)
                .date(LocalDateTime.now(Clock.systemUTC()))
                .message(exception.getMessage())
                .description(request.getDescription(false))
                .build();
        return new ResponseEntity<>(message, HttpStatus.valueOf(status));
    }

}