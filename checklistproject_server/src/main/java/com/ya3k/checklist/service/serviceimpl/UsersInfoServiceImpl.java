package com.ya3k.checklist.service.serviceimpl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ya3k.checklist.dto.UsersInfoDto;
import com.ya3k.checklist.entity.UserInfo;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.mapper.UsersInfoMapper;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.repository.UsersInfoRepository;
import com.ya3k.checklist.service.KeyCloakService;
import com.ya3k.checklist.service.serviceinterface.UserInfoService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class UsersInfoServiceImpl implements UserInfoService {

    @Value("${keycloak.client-id}")
    private String CLIENT_ID;
    @Value("${keycloak.client-secret}")
    private String CLIENT_SECRET;
    @Value("${keycloak.introspect-url}")
    private String INTROSPECT_URL;

    private final UserRepository userRepository;
    private final UsersInfoRepository usersInfoRepository;
    private final RestTemplate restTemplate;

    private final KeyCloakService keyCloakService;

    public UsersInfoServiceImpl(UserRepository userRepository, UsersInfoRepository usersInfoRepository, RestTemplate restTemplate, KeyCloakService keyCloakService) {
        this.userRepository = userRepository;
        this.usersInfoRepository = usersInfoRepository;
        this.restTemplate = restTemplate;
        this.keyCloakService = keyCloakService;
    }


    public void getUserInfo(String token) {
        // Introspect the token
        boolean active = keyCloakService.introspectToken(token);
        if (!active) {
            throw new RuntimeException("Token is not active");
        }

        // Get the user info
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("client_id", CLIENT_ID);
        body.add("client_secret", CLIENT_SECRET);
        body.add("token", token);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response;
        try {
            response = restTemplate.postForEntity(INTROSPECT_URL, entity, String.class);
        } catch (Exception e) {
            throw new RuntimeException("Error while introspecting token", e);
        }

        // Parse JSON string to JsonNode
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode;
        try {
            jsonNode = mapper.readTree(response.getBody());
            String username =jsonNode.get("username").asText();
            String email = jsonNode.get("email").asText();
            String fullName = jsonNode.get("given_name").asText()+ " " +jsonNode.get("family_name").asText();

            // Save the user info
            UsersInfoDto usersInfoDto = new UsersInfoDto();
            usersInfoDto.setFullName(fullName);
            usersInfoDto.setEmail(email);

            createUserInfo(usersInfoDto, username);

        } catch (Exception e) {
            throw new RuntimeException("Error while parsing response", e);
        }


    }


    @Override
    public UsersInfoDto createUserInfo(UsersInfoDto usersInfoDto, String userName) {
        Users getUsers = userRepository.findByUser(userName);

        if (getUsers == null) {
            throw new RuntimeException("User not found");
        }

        // Check if UserInfo already exists for the user
        UserInfo existingUserInfo = usersInfoRepository.findByUser(getUsers);
        if (existingUserInfo != null) {
            // Update existing UserInfo with new information
            existingUserInfo.setFullName(usersInfoDto.getFullName());
            existingUserInfo.setEmail(usersInfoDto.getEmail());
            // No need to save again as the entity is managed
            // by JPA and changes will be reflected in the database
            return UsersInfoMapper.mapToDto(existingUserInfo);
        }

        // If UserInfo does not exist, create a new one
        UserInfo newUserInfo = new UserInfo();
        newUserInfo.setFullName(usersInfoDto.getFullName());
        newUserInfo.setEmail(usersInfoDto.getEmail());
        newUserInfo.setUser(getUsers); // Associate UserInfo with the user
        UserInfo savedUserInfo = usersInfoRepository.save(newUserInfo);
        return UsersInfoMapper.mapToDto(savedUserInfo);
    }

}
