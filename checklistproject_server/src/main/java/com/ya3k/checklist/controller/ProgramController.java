package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.response.ProgramListResponse;
import com.ya3k.checklist.response.ProgramResponse;
import org.springframework.beans.factory.annotation.Autowired;
import com.ya3k.checklist.service.serviceinterface.ProgramService;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.List;


@RestController
@RequestMapping("/programs")
@CrossOrigin(origins = "${front-end.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
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

    //search program


    @GetMapping("/search")
    public ResponseEntity<List<ProgramDto>> searchProgram(
            @RequestHeader(name = "user_name") String userName,
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


    @GetMapping("/all")
    public ResponseEntity<?> getAllPrograms(
            @RequestHeader(name = "user_name") String userName,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page -1, size);
        try {
            Page<ProgramResponse> programsList = programService.findByUserName(userName, pageable);
            int totalPage = programsList.getTotalPages();
            int totalElements = (int) programsList.getTotalElements();

            List<ProgramResponse> programs = programsList.getContent();
            return ResponseEntity.ok(ProgramListResponse.builder()
                    .programResponseList(programs)
                    .totalPage(totalPage)
                    .total(totalElements)
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
