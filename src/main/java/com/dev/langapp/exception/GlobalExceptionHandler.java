package com.dev.langapp.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(StudentNotFoundException.class)
    public ProblemDetail handleStudentNotFound(StudentNotFoundException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.NOT_FOUND,
                "Nenhum estudante com o uuid " + e.getMessage()
        );
        problem.setTitle("Estudante não encontrado");
        return problem;
    }

    @ExceptionHandler(StudentAlreadyExistsException.class)
    public ProblemDetail handleStudentAlreadyExists(StudentAlreadyExistsException e) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(
                HttpStatus.CONFLICT,
                "Já existe aluno com esse UUID " +  e.getMessage()
        );
        problem.setTitle("Cadastro Duplicado");
        return problem;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
        Map<String, String> erros = new HashMap<>();

        for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
            erros.put(fieldError.getField(), fieldError.getDefaultMessage());
        }

        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Dados Inválidos");
        problem.setProperty("errors", erros);
        return problem;
    }
}
