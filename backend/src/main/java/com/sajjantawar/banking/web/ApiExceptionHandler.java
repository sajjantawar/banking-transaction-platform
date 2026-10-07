package com.sajjantawar.banking.web;
import jakarta.persistence.EntityNotFoundException; import org.springframework.http.*; import org.springframework.web.bind.MethodArgumentNotValidException; import org.springframework.web.bind.annotation.*; import java.time.Instant; import java.util.stream.Collectors;
@RestControllerAdvice public class ApiExceptionHandler {
 @ExceptionHandler(EntityNotFoundException.class) ResponseEntity<ApiError> notFound(EntityNotFoundException e){return response(HttpStatus.NOT_FOUND,e.getMessage());}
 @ExceptionHandler(IllegalArgumentException.class) ResponseEntity<ApiError> badRequest(IllegalArgumentException e){return response(HttpStatus.BAD_REQUEST,e.getMessage());}
 @ExceptionHandler(SecurityException.class) ResponseEntity<ApiError> forbidden(SecurityException e){return response(HttpStatus.FORBIDDEN,e.getMessage());}
 @ExceptionHandler(IllegalStateException.class) ResponseEntity<ApiError> conflict(IllegalStateException e){return response(HttpStatus.CONFLICT,e.getMessage());}
 @ExceptionHandler(MethodArgumentNotValidException.class) ResponseEntity<ApiError> validation(MethodArgumentNotValidException e){String message=e.getBindingResult().getFieldErrors().stream().map(x->x.getField()+": "+x.getDefaultMessage()).collect(Collectors.joining(", "));return response(HttpStatus.BAD_REQUEST,message);}
 private ResponseEntity<ApiError> response(HttpStatus s,String m){return ResponseEntity.status(s).body(new ApiError(Instant.now(),s.value(),s.getReasonPhrase(),m));}
 public record ApiError(Instant timestamp,int status,String error,String message){}
}
