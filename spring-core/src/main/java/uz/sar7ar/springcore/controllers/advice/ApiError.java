package uz.sar7ar.springcore.controllers.advice;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

/**
 * This class represents an API error response.
 * It contains the HTTP status, a message, and a list of errors.
 */
@AllArgsConstructor
@Getter
public class ApiError {
    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    public ApiError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = Collections.singletonList(error);
    }
}
