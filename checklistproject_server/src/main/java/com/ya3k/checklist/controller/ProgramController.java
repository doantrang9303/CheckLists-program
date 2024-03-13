package com.ya3k.checklist.controller;

import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.service.serviceimpl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;


@RestController
@RequestMapping("/programs")
@CrossOrigin(origins = "${front-end.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class ProgramController {
    @Autowired
    ProgramRepository repo;
    @Autowired
    UserRepository urepo;


    @PostMapping("/add")
    public ResponseEntity<?> createProgram(@RequestBody Program program, @RequestHeader String userName) {
        if(userName==null || userName.isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is empty");
        }
        Users user = urepo.findByUser(userName);
        if(user==null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
        }

        program.setUser(user);
        if (program.getStatus() == null || program.getStatus().equals(""))
            program.setStatus("IN_PROGRESS");
        else program.setStatus(program.getStatus());
        program.setCreate_time(LocalDateTime.now());
        Program savedProgram = repo.save(program);
        return ResponseEntity.ok(savedProgram);
    }



}
