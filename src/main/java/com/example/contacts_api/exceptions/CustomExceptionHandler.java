package com.example.contacts_api.exceptions;

import com.example.contacts_api.dto.ErrorDTO;
import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class CustomExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ErrorDTO> handleBadRequestException(BadRequestException ex, WebRequest request) {
        logger.error("Exception during execution of application:", ex);

        ErrorDTO errorDTO = ErrorDTO.builder()
                .error("Bad Request")
                .errorDescription(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PhoneNumberAlreadyExistsException.class)
    public ResponseEntity<ErrorDTO> handlePhoneNumberAlreadyExistsException(PhoneNumberAlreadyExistsException ex, WebRequest request) {
        logger.error("Phone number conflict occurred:", ex);

        ErrorDTO errorDTO = ErrorDTO.builder()
                .error("Conflict")
                .errorDescription(ex.getMessage())
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.CONFLICT);
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleGenericException(Exception ex, WebRequest request) {
        logger.error("Exception during execution of application:", ex);

        ErrorDTO errorDTO = ErrorDTO.builder()
                .error("Internal Server Error")
                .errorDescription("An unexpected error occurred.")
                .build();

        return new ResponseEntity<>(errorDTO, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
