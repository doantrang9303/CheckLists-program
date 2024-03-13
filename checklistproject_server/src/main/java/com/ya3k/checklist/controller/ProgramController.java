package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import lombok.Data;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/programs")
public class ProgramController {
    ProgramRepository repo;
    UserRepository urepo;
    private ProgramService programService;

    @Autowired
    public ProgramController(ProgramRepository repo, UserRepository urepo, ProgramService programService) {
        this.repo = repo;
        this.urepo = urepo;
        this.programService = programService;
    }


    @PostMapping("/add")
    public ResponseEntity<Program> createProgram(@RequestBody Program program, @RequestHeader Long id) {
        Users user = urepo.findById(id).orElseThrow();
        program.setUser(user);
        if (program.getStatus() == null || program.getStatus().equals(""))
            program.setStatus("IN_PROGRESS");
        else program.setStatus(program.getStatus());
        program.setCreate_time(LocalDateTime.now());
        Program savedProgram = repo.save(program);
        return ResponseEntity.ok(savedProgram);
    }

    //search program


    @GetMapping("/search")
    public ResponseEntity<List<ProgramDto>> searchProgram(
            @RequestParam(name = "user_name") String userName,
            @RequestParam(name = "p_name") String name,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
            ) {
        Pageable pageable = PageRequest.of(page -1, size);
        List<ProgramDto> programs = programService.seachProgramName(userName, name, pageable);
        if(programs == null || programs.isEmpty())
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok(programs);
    }




}
