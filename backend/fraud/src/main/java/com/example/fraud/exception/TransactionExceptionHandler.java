// This class is part of the 'exception' package in your application.
// You can create this directory inside: src/main/java/com/example/fraud/
package com.example.fraud.exception;

// ---------------------------
// 💡 SPRING FRAMEWORK IMPORTS
// ---------------------------

// Handles HTTP status codes like 400, 404, 500, etc.
// Comes from: org.springframework.http
import org.springframework.http.HttpStatus;

// Represents a full HTTP response, including body and status
// Comes from: org.springframework.http
import org.springframework.http.ResponseEntity;

// This exception is thrown automatically by Spring when a @Valid object fails validation
// Comes from: org.springframework.web.bind
import org.springframework.web.bind.MethodArgumentNotValidException;

// Tells Spring that this class will handle exceptions across the whole app (all controllers)
// Comes from: org.springframework.web.bind.annotation
import org.springframework.web.bind.annotation.ControllerAdvice;

// Used to define methods that should handle specific exceptions
// Comes from: org.springframework.web.bind.annotation
import org.springframework.web.bind.annotation.ExceptionHandler;

// ---------------------------
// 💡 JAVA UTILITY IMPORTS
// ---------------------------

// Provides a resizable, unsorted key-value map (used to send the response body as JSON)
// Comes from: java.util
import java.util.HashMap;

// Provides the base Map interface (used as a return type for JSON response)
// Comes from: java.util
import java.util.Map;

// Represents a list of elements (our validation error messages)
// Comes from: java.util
import java.util.List;

// Provides stream operations like map/filter/collect (used to process error messages)
// Comes from: java.util.stream
import java.util.stream.Collectors;


@ControllerAdvice  // 💡 This tells Spring Boot: "Apply this advice to all controllers"
public class TransactionExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)  // 💡 Catches only validation errors
    public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {

        // Step 1: Extract error messages from the exception
        List<String> errors = ex.getBindingResult()        // 💡 Get the result of the validation
                .getFieldErrors()                          // 💡 Get field-level validation errors
                .stream()                                  // 💡 Convert to stream to process
                .map(error -> error.getDefaultMessage())   // 💡 Extract the message for each error
                .collect(Collectors.toList());             // 💡 Collect into a list of strings

        // Step 2: Create a response body using a HashMap
        Map<String, Object> response = new HashMap<>();    // 💡 Generic key-value pair map
        response.put("error", "Validation failed");        // 💡 Custom error title
        response.put("details", errors);                   // 💡 The list of error messages

        // Step 3: Return the response with HTTP status 400 (Bad Request)
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);  // 💡 Wrap map into a full response
    }
}
