package com.ya3k.checklist.services;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@AllArgsConstructor
@Service
public class KeyCloakService {

    private final String CLIENT_ID = "auth-client";
    private final String CLIENT_SECRET = "hbtIpc8gZLyn0fbtHLEBik1h731cfHrm";
    private final String INTROSPECT_URL = "http://localhost:8080/realms/ya3ktest/protocol/openid-connect/token/introspect";


    @Autowired
    private final RestTemplate restTemplate;

    public Boolean introspectToken(String token) throws Exception {

        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();


        //ENV!!!!!!!!!!!!
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);

        // access token
        body.add("token", token);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);


        //ENV!!!!!!!!!!!!
        //url introspect verify token
        String url = INTROSPECT_URL;

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);


        // Parse JSON string to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(response.getBody());

        // Get the value of "active"
        boolean isActive = jsonNode.get("active").asBoolean();

        return isActive;

    }
}
