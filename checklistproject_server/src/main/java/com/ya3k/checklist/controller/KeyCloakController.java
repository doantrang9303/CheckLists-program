package com.ya3k.checklist.controller;

import com.ya3k.checklist.services.KeyCloakService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class KeyCloakController {
    @Autowired
    private final KeyCloakService keycloakService;
    //method post
    //verify token
    @PostMapping("/verify-token")
    private ResponseEntity<?> verifyToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws Exception {
        //check if token is empty
        if (token == null || token.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is Empty");
        }
        //check length of token
        if (!token.startsWith("Bearer ")){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing");
        }
        //substring Bearer
        token = token.substring(7);
        boolean isActive = keycloakService.introspectToken(token);
        //check token true or false
        if (isActive) {
            return ResponseEntity.status(HttpStatus.OK).body("True");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("False");
        }

    }

    @GetMapping("/test")
    public ResponseEntity<String> testGet() {
        return ResponseEntity.status(HttpStatus.OK).body("Hello");
    }

    //test function
    
    @PostMapping("/test")
    public ResponseEntity<String> test(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) {
        if (token.length() < 7) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing");
        }
        token = token.substring(7);
        if (token.equals("")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing");
        }
        //api verify token
        //parse result -> active ? true : false

        if (token.equals("123456")) {
            return ResponseEntity.status(HttpStatus.OK).body("True");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("False");
        }
    }




}
