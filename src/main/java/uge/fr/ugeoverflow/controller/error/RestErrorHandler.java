package uge.fr.ugeoverflow.controller.error;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import uge.fr.ugeoverflow.error.answer.AnswerRuntimeException;
import uge.fr.ugeoverflow.error.question.QuestionNotFoundException;
import uge.fr.ugeoverflow.error.question.QuestionRuntimeException;
import uge.fr.ugeoverflow.error.user.TokenAlreadyExistException;
import uge.fr.ugeoverflow.utils.ResponseMessage;

import java.util.stream.Collectors;

@ControllerAdvice(basePackages = "uge.fr.ugeoverflow.controller.rest")
public class RestErrorHandler {

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<?> handleValidationException(ConstraintViolationException ex) {
        var errors = ex.getConstraintViolations().stream()
                .map(ConstraintViolation::getMessage)
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("ConstraintViolationException", errors));
    }


    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        String message = String.format("Invalid value '%s' for parameter '%s'.", ex.getValue(), ex.getName());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Argument TypeMismatch", message));
    }

    @ExceptionHandler({TokenAlreadyExistException.class})
    public ResponseEntity<Object> handleException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ResponseMessage("Internal server error", ex.getMessage()));
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(Exception ex) {
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE).body(new ResponseMessage("Authentication error", ex.getMessage()));
    }

    @ExceptionHandler(QuestionRuntimeException.class)
    public ResponseEntity<Object> handleQuestionRuntimeException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Question error", ex.getMessage()));
    }

    @ExceptionHandler(AnswerRuntimeException.class)
    public ResponseEntity<Object> handleAnswerRuntimeException(Exception ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Answer error", ex.getMessage()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<?> handleHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage("Request error", "The request body is missing"));
    }




}
