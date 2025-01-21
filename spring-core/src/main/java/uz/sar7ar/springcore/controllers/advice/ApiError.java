package uz.sar7ar.springcore.controllers.advice;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import java.util.Collections;
import java.util.List;

@Getter
@RequiredArgsConstructor
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

