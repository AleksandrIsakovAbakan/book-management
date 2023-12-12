package com.example.bookmanagement.exception;

import com.example.bookmanagement.api.v1.response.ErrorRs;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionHandlerAdvice {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorRs> handleEntityNotFoundException(EntityNotFoundException e) {
        return ResponseEntity.badRequest().body(makeErrors("EntityNotFoundException", e));
    }

    @ExceptionHandler(AlreadySuchNameException.class)
    public ResponseEntity<ErrorRs> handleAlreadySuchNameException(
            AlreadySuchNameException ex) {
        log.info("AlreadySuchNameException " + ex);
        return ResponseEntity.badRequest().body(makeErrors("AlreadySuchNameException", ex));
    }

    @ExceptionHandler(ArrayIndexOutOfBoundsException.class)
    public ResponseEntity<ErrorRs> handleArrayIndexOutOfBoundsException(
            ArrayIndexOutOfBoundsException ex) {
        log.info("ArrayIndexOutOfBoundsException " + ex);
        return ResponseEntity.badRequest().body(makeErrors("ArrayIndexOutOfBoundsException", ex));
    }


    private ErrorRs makeErrors(String error, Exception e){
        ErrorRs errorRs = new ErrorRs();
        errorRs.setError(error);
        errorRs.setErrorDescription(e.getMessage());
        errorRs.setTimestamp(System.currentTimeMillis());

        return errorRs;
    }

}
