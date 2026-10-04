package com.fatec.tarefas.exception;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(TarefaNaoEncontradaException.class)
    public ResponseEntity<Map<String, String>> naoEncontrada(TarefaNaoEncontradaException exception) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("erro", exception.getMessage()));
    }

    @ExceptionHandler(RegraNegocioException.class)
    public ResponseEntity<Map<String, String>> regraNegocio(RegraNegocioException exception) {
        return ResponseEntity.badRequest().body(Map.of("erro", exception.getMessage()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> validacao(MethodArgumentNotValidException exception) {
        String mensagem = exception.getBindingResult().getFieldErrors().stream()
                .map(erro -> erro.getField() + " " + erro.getDefaultMessage())
                .findFirst()
                .orElse("Requisição inválida");
        return ResponseEntity.badRequest().body(Map.of("erro", mensagem));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> corpoInvalido(HttpMessageNotReadableException exception) {
        return ResponseEntity.badRequest().body(Map.of("erro", "Corpo da requisição inválido"));
    }
}
