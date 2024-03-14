package com.ya3k.checklist.controller;

import com.ya3k.checklist.service.KeyCloakService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TestController {
    @Autowired
    private final KeyCloakService keycloakService;

    //method post
    //verify token
    @PostMapping("/verify-2")
    private ResponseEntity<?> verifyToken(@RequestHeader(HttpHeaders.AUTHORIZATION) String token) throws IOException, InterruptedException {
        // Check if token is empty or missing "Bearer" prefix
        if (token == null || token.isEmpty() || !token.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token is missing or invalid");
        }

        // Extract token without "Bearer" prefix
        token = token.substring(7);

        HttpClient client = HttpClient.newHttpClient();
        String keycloakTokenUrl = "http://localhost:8080/realms/ya3ktest/protocol/openid-connect/token/introspect";
        Map<String, String> data = new HashMap<>();
        data.put("client_id", "auth-client");
        data.put("token", token);
        data.put("client_secret", "EOTSj7klMyl84TyEzGSlynKmYEGzRBuS");

        // Build form body
        StringBuilder formBody = new StringBuilder();
        for (Map.Entry<String, String> entry : data.entrySet()) {
            if (formBody.length() > 0) {
                formBody.append("&");
            }
            formBody.append(entry.getKey()).append("=").append(entry.getValue());
        }

        // Build the request
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(keycloakTokenUrl))
                .header("Content-Type", "application/x-www-form-urlencoded")
                .POST(HttpRequest.BodyPublishers.ofString(formBody.toString()))
                .build();

        // Send the request and retrieve the response
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        // Return the response status code and body
        return ResponseEntity.status(response.statusCode()).body(response.body());
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
            return ResponseEntity.status(HttpStatus.OK).body("False");
        }

    }


}