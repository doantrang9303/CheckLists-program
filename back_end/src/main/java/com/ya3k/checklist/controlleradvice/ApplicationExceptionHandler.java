package com.ya3k.checklist.controlleradvice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.format.DateTimeParseException;
import java.util.*;

@RestControllerAdvice
public class ApplicationExceptionHandler {


    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, List<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        // Create a map to store errors, where each key represents a category of errors
        Map<String, List<Map<String, String>>> errors = new HashMap<>();

        // Create a list to store individual error maps
        List<Map<String, String>> errorList = ex.getBindingResult().getFieldErrors().stream()
                .map(fieldError -> {
                    // Create a new error map for each field error
                    Map<String, String> errorMap = new HashMap<>();
                    // Add field name and error message to the error map
                    errorMap.put("field", fieldError.getField());  // Key "field" for field name
                    errorMap.put("message", fieldError.getDefaultMessage());  // Key "message" for error message
                    return errorMap;
                })
                .toList();  // Collect error maps into a list

        // Add the error list to the errors map with a specific key ("invalid_input")
        errors.put("invalid_input", errorList);

        return errors;  // Return the errors map with categorized error lists
    }

    //   Exception handler for handling DateTimeFormat parsing errors
    @ExceptionHandler(DateTimeParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Map<String, String> handleDateTimeFormatException(DateTimeParseException e) {
        Map<String, String> error = new HashMap<>();
        error.put("message", "Invalid date format for endTime, please use yyyy-MM-dd format.");
        return error;
    }


}
