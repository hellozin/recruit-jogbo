package univ.study.recruitjogbo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import univ.study.recruitjogbo.api.response.ApiError;
import univ.study.recruitjogbo.api.response.ApiResponse;
import univ.study.recruitjogbo.error.ServiceRuntimeException;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    private ResponseEntity<ApiResponse> newResponse(String errorMessage, HttpStatus status) {
        return new ResponseEntity<>(ApiResponse.ERROR(new ApiError(errorMessage, status)), status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        String validationErrorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> String.format("Field: [%s] Message: %s Input: [%s]",
                        fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue()))
                .collect(Collectors.joining(","));
        log.error("Bad request exception : [{}]", e.getMessage());
        return newResponse(validationErrorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ServiceRuntimeException.class)
    public ResponseEntity<?> handleServiceRuntimeException(ServiceRuntimeException e) {
        log.error("Service runtime exception : [{}]", e.getMessage());
        return newResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleBadRequestException(Exception e) {
        log.error("Bad request exception : [{}]", e.getMessage());
        return newResponse(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<?> handleUnknownException(Exception e) {
        log.error("Unknown exception : [{}]", e.getMessage());
        return newResponse(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
