package com.ya3k.checklist.controller;

import com.ya3k.checklist.service.serviceimpl.UsersInfoServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/userinfo")
public class UserInfoController {
    private final UsersInfoServiceImpl usersInfoService;


    public UserInfoController(UsersInfoServiceImpl usersInfoService) {
        this.usersInfoService = usersInfoService;
    }

    // Add use info for user
    @PostMapping("/add")
    public ResponseEntity<?> addUserInfo(@RequestHeader String token){
        if(token == null){
            return ResponseEntity.badRequest().body("Token is required");
        }
        usersInfoService.getUserInfo(token);
        return ResponseEntity.ok("User info added successfully");
    }
}
