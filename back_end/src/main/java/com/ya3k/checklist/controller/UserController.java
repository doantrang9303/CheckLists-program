package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.UsersDto;
import com.ya3k.checklist.service.serviceimpl.UserServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/users")
@CrossOrigin(origins = "${frontend.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class UserController {
    private UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUserInfo(@RequestHeader String userName, @RequestHeader String email) {
        try {
            UsersDto usersDto = new UsersDto();
            usersDto.setUserName(userName);
            usersDto.setEmail(email);
            userServiceImpl.saveUsers(usersDto);
            log.info("Add info of user: {}  is successfully", usersDto.getUserName());
            return ResponseEntity.ok("User added successfully");
        } catch (RuntimeException e) {
            log.error("Error while adding user: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.OK).body(e.getMessage());
        } catch (Exception e) {
            log.error("Unexpected error while adding user: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error while adding user");
        }
    }
}
