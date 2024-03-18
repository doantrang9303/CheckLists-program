package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.response.programresponse.ProgramListResponse;
import com.ya3k.checklist.response.programresponse.ProgramResponse;
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
    public ResponseEntity<?> searchProgram(
            @RequestHeader(name = "user_name") String userName,
            @RequestParam(name = "p_name") String name,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
            ) {
        Pageable pageable = PageRequest.of(page -1, size);
      try{
            Page<ProgramResponse> programsList = programService.seachProgramName(userName, name, pageable);
            int totalPage = programsList.getTotalPages();
            int totalElements = (int) programsList.getTotalElements();

            List<ProgramResponse> programs = programsList.getContent();
            return ResponseEntity.ok(ProgramListResponse.builder()
                    .programResponseList(programs)
                    .totalPage(totalPage)
                    .total(totalElements)
                    .build());
      }catch (Exception e){
          return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
      }
    }


    @GetMapping("/all")
    public ResponseEntity<?> getAllPrograms(
            @RequestHeader(name = "user_name") String userName,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page -1, size);
        try {
            Page<ProgramResponse> programsList = programService.findProgramByUserName(userName, pageable);
            int totalPage = programsList.getTotalPages();
            int totalElements = (int) programsList.getTotalElements();

            List<ProgramResponse> programs = programsList.getContent();
            return ResponseEntity.ok(ProgramListResponse.builder()
                    .programResponseList(programs)
                    .totalPage(totalPage)
                    .total(totalElements)
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/filter")
    public ResponseEntity<?> getProgramsByFilters(
            @RequestHeader(name = "user_name") String userName,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "program_name", required = false) String programName,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page -1, size);
        try {
            Page<ProgramResponse> programsList = programService.findByUserAndFilters(userName, status, programName, pageable);
            int totalPage = programsList.getTotalPages();
            int totalElements = (int) programsList.getTotalElements();

            List<ProgramResponse> programs = programsList.getContent();
            return ResponseEntity.ok(ProgramListResponse.builder()
                    .programResponseList(programs)
                    .totalPage(totalPage)
                    .total(totalElements)
                    .build());
        }catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProgram(@PathVariable int id) {
        if(id < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program ID must be greater than 0");
        }
        try {
            ProgramDto findProgram = programService.findByProgramId(id);
            if(findProgram == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Program not found");
            }
            else{
                ProgramDto program = programService.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(findProgram.getName() + " deleted successfully");

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
