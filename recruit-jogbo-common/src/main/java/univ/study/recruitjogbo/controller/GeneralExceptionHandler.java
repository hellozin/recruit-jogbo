package univ.study.recruitjogbo.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import univ.study.recruitjogbo.api.response.ApiError;
import univ.study.recruitjogbo.api.response.ApiResponse;

import java.util.stream.Collectors;

@ControllerAdvice
@Slf4j
public class GeneralExceptionHandler {

    private ResponseEntity<ApiResponse> newResponse(String errorMessage, HttpStatus status) {
        return new ResponseEntity<>(ApiResponse.ERROR(new ApiError(errorMessage, status)), status);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidationException(MethodArgumentNotValidException e) {
        String validationErrorMessage = e.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining("\n"));
        log.error("Bad request exception : [{}]", e.getMessage());
        return newResponse(validationErrorMessage, HttpStatus.BAD_REQUEST);
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
