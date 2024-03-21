package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.dto.response.programresponse.ProgramListResponse;
import com.ya3k.checklist.dto.response.programresponse.ProgramResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import com.ya3k.checklist.service.serviceinterface.ProgramService;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;


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
    public ResponseEntity<?> createProgram(@RequestBody @Valid ProgramDto programDto,
                                           @RequestHeader String user_name) {
        try {


            if (user_name == null || user_name.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is empty");
            }
            Users user = urepo.findByUser(user_name);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
            }

            ProgramDto savedProgram = programService.createProgram(programDto, user_name);
            return ResponseEntity.ok(savedProgram);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }

    }


    //get list program with filter or not
    @GetMapping()
    public ResponseEntity<?> getProgramsByFilters(
            @RequestHeader(name = "user_name") String userName,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "end_time", required = false) LocalDate endTime,
            @RequestParam(name = "program_name", required = false) String programName,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        try {
            Page<ProgramResponse> programsList = programService.findByUserAndFilters(userName, status, endTime, programName, pageable);
            int totalPage = programsList.getTotalPages();
            int totalElements = (int) programsList.getTotalElements();
            List<ProgramResponse> programs = programsList.getContent();

            return ResponseEntity.ok(ProgramListResponse.builder()
                    .programResponseList(programs)
                    .totalPage(totalPage)
                    .total(totalElements)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProgram(@PathVariable int id) {
        if (id < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program ID must be greater than 0");
        }
        try {
            ProgramDto findProgram = programService.findByProgramId(id);
            if (findProgram == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Program not found");
            } else {
                ProgramDto program = programService.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(findProgram.getName() + " deleted successfully");

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    //    @PostMapping("/add")
//    public ResponseEntity<?> createProgram(@RequestBody Program program, @RequestHeader String user_name) {
//        if(user_name==null || user_name.isEmpty()){
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is empty");
//        }
//        Users user = urepo.findByUser(user_name);
//        if(user==null){
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
//        }
//
//        program.setUser(user);
//        if (program.getStatus() == null || program.getStatus().isEmpty())
//            //set default status to IN_PROGRESS
//            program.setStatus(StatusEnum.IN_PROGRESS.getStatus());
//        else program.setStatus(program.getStatus());
//        program.setCreate_time(LocalDateTime.now());
//        Program savedProgram = repo.save(program);
//        return ResponseEntity.ok(savedProgram);
//    }

}
