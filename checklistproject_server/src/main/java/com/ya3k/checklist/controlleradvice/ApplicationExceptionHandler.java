package com.ya3k.checklist.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestControllerAdvice
public class ApplicationExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, List<Map<String, String>>> handleInvalidArgument(MethodArgumentNotValidException ex) {
        Map<String, List<Map<String, String>>> errorMap = new HashMap<>();
        List<Map<String, String>> errorList = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Map<String, String> fieldError = new HashMap<>();
            fieldError.put(error.getField(), error.getDefaultMessage());

            errorList.add(fieldError);
        });
        errorMap.put("invalid_input", errorList);
        return errorMap;
    }


}
