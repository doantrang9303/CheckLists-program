package com.ya3k.checklist.controller;

import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/program")
public class ProgramController {
    @Autowired
    ProgramRepository repo;
    @Autowired
    UserRepository urepo;

    @PostMapping("/add")
   public ResponseEntity<Program> createProgram(@RequestBody  Program program,@RequestHeader Long id)  {
        Users user = urepo.findById(id).orElseThrow();
            program.setUser(user);
       if(program.getStatus()==null || program.getStatus().equals(""))
           program.setStatus("IN_PROGRESS");
        else program.setStatus(program.getStatus());
        program.setCreate_time(LocalDateTime.now());
        Program savedProgram = repo.save(program);
        return ResponseEntity.ok(savedProgram);
    }



}
