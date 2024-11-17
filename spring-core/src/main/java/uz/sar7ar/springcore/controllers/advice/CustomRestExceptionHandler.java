package uz.sar7ar.springcore.controllers.advice;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import uz.sar7ar.springcore.exceptions.InvalidUserPasswordException;
import uz.sar7ar.springcore.exceptions.UserNameNotFoundException;
import uz.sar7ar.springcore.exceptions.UserPasswordConfirmationException;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a controller advice that handles exceptions thrown by the application.
 * It extends the ResponseEntityExceptionHandler class provided by Spring.
 * The class is annotated with @ControllerAdvice, which means that it is a global exception handler.
 */
@ControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    /**
     * This method is invoked when a @Valid validation fails. It creates
     * an ApiError instance and calls {@link #handleExceptionInternal(Exception, Object, HttpHeaders, HttpStatusCode, WebRequest)}
     * passing the ApiError as the body of the response.
     *
     * @param ex the exception
     * @param headers the HttpHeaders
     * @param status the HttpStatusCode
     * @param request the WebRequest
     * @return the ResponseEntity
     */
    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        List<String> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> errors.add(error.getField() + ":" +
                                                                           error.getDefaultMessage()));
        ex.getBindingResult().getGlobalErrors().forEach(error -> errors.add(error.getObjectName() + ": " +
                                                                            error.getDefaultMessage()));
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

/**
 * Handles MissingServletRequestParameterException by creating an ApiError instance
 * with details about the missing parameter and returns a ResponseEntity with a
 * BAD_REQUEST status.
 *
 * @param ex the MissingServletRequestParameterException containing details about the missing parameter
 * @param headers the HttpHeaders to be included in the response
 * @param status the HttpStatusCode representing the response status
 * @param request the WebRequest during which the exception occurred
 * @return a ResponseEntity containing the ApiError with a BAD_REQUEST status
 */
    @Override
    protected ResponseEntity<Object> handleMissingServletRequestParameter(MissingServletRequestParameterException ex,
                                                                          HttpHeaders headers,
                                                                          HttpStatusCode status,
                                                                          WebRequest request) {
        String error = ex.getParameterName() + " parameter is missing";
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);
        return ResponseEntity
                .status(apiError.getStatus())
                .headers(new HttpHeaders())
                .body(apiError);
    }

    /**
     * Handles ConstraintViolationException by creating an ApiError instance
     * with details about the violated constraint and returns a ResponseEntity with a
     * BAD_REQUEST status.
     *
     * @param ex the ConstraintViolationException containing details about the violated constraint
     * @param request the WebRequest during which the exception occurred
     * @return a ResponseEntity containing the ApiError with a BAD_REQUEST status
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<Object> handleConstraintViolation(ConstraintViolationException ex, WebRequest request){
        List<String> errors = new ArrayList<>();
        ex.getConstraintViolations().forEach(violation -> errors.add(violation.getRootBeanClass().getName() + " :: " +
                                                                     violation.getPropertyPath() + " :: " +
                                                                     violation.getMessage()));
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);

        return ResponseEntity
                .status(apiError.getStatus())
                .headers(new HttpHeaders())
                .body(apiError);
    }

    /**
     * Handles MethodArgumentTypeMismatchException by creating an ApiError instance
     * with details about the type mismatch and returns a ResponseEntity with a
     * BAD_REQUEST status.
     *
     * @param ex the MethodArgumentTypeMismatchException containing details about the type mismatch
     * @param request the WebRequest during which the exception occurred
     * @return a ResponseEntity containing the ApiError with a BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public  ResponseEntity<Object> handleMethodArgumentTypeMismatch(MethodArgumentTypeMismatchException ex,
                                                                    WebRequest request){
        String error = ex.getName() + " should be of type " + ex.getRequiredType().getName();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), error);

        return ResponseEntity
                .status(apiError.getStatus())
                .headers(new HttpHeaders())
                .body(apiError);
    }

    /**
     * Handles HttpRequestMethodNotSupportedException by creating an ApiError instance
     * with details about the unsupported http method and returns a ResponseEntity with a
     * METHOD_NOT_ALLOWED status.
     *
     * @param ex the HttpRequestMethodNotSupportedException containing details about the unsupported http method
     * @param headers the HttpHeaders to be included in the response
     * @param status the HttpStatusCode representing the response status
     * @param request the WebRequest during which the exception occurred
     * @return a ResponseEntity containing the ApiError with a METHOD_NOT_ALLOWED status
     */
    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
                                                                         HttpHeaders headers,
                                                                         HttpStatusCode status,
                                                                         WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append(" method is not supported for this request. Supported methods are ");
        ex.getSupportedHttpMethods().forEach(t -> builder.append(t).append(" "));

        ApiError apiError = new ApiError(HttpStatus.METHOD_NOT_ALLOWED,
                ex.getLocalizedMessage(),
                builder.toString());

        return ResponseEntity
                .status(apiError.getStatus())
                .headers(new HttpHeaders())
                .body(apiError);
    }

    /**
     * Handles any other exceptions that may be thrown by the application.
     *
     * @param ex the exception that was thrown
     * @param request the WebRequest during which the exception occurred
     * @return a ResponseEntity containing the ApiError with an INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        ApiError apiError = new ApiError(HttpStatus.INTERNAL_SERVER_ERROR,
                                         ex.getLocalizedMessage(),
                                         "error occurred");

        return ResponseEntity
                .status(apiError.getStatus())
                .headers(new HttpHeaders())
                .body(apiError);
    }

    /**
     * Handles UserPasswordConfirmationException by returning a ResponseEntity with a status of BAD_REQUEST.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing the localized message of the exception
     */
    @ExceptionHandler(UserPasswordConfirmationException.class)
    public ResponseEntity<Object> handleUserPasswordConfirmationException(UserPasswordConfirmationException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getLocalizedMessage());
    }

    /**
     * Handles UserNameNotFoundException by returning a ResponseEntity with a status of BAD_REQUEST.
     *
     * @param ex the exception that was thrown
     * @return a ResponseEntity containing the localized message of the exception
     */
    @ExceptionHandler(UserNameNotFoundException.class)
    public ResponseEntity<Object> handleUserNameNotFoundException(UserNameNotFoundException ex){

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getLocalizedMessage());
    }

    /**
     * Handles InvalidUserPasswordException by returning a ResponseEntity with a status of BAD_REQUEST.
     *
     * @param ex the InvalidUserPasswordException that was thrown
     * @return a ResponseEntity containing the localized message of the exception
     */
    @ExceptionHandler(InvalidUserPasswordException.class)
    public ResponseEntity<Object> handleInvalidUserPasswordException(InvalidUserPasswordException ex){
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ex.getLocalizedMessage());
    }
}
