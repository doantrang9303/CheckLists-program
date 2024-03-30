package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.dto.response.programresponse.ProgramListResponse;
import com.ya3k.checklist.dto.response.programresponse.ProgramResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.servers.Servers;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@RestController
@RequestMapping("/programs")
@CrossOrigin(origins = "${front-end.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Tag(name = "Programs API", description = "APIs for Programs")
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


    @Operation(summary = "Create a new Program", description = "Create a new Program")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add Program Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Body"),
            @ApiResponse(responseCode = "401", description = "User Not Found")
    })

    @PostMapping("/add")
    public ResponseEntity<?> createProgram(@RequestBody @Valid ProgramDto programDto,
                                           @RequestHeader String user_name) {
        try {
            log.debug("Received request to create a new program");


            if (user_name == null || user_name.isEmpty()) {
                log.debug("username is empty");
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username is empty");
            }
            Users user = urepo.findByUser(user_name);
            if (user == null) {
                log.debug("User not found for username: {}", user_name);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User Not Found");
            }

            log.debug("User found: {}", user);


            ProgramDto savedProgram = programService.createProgram(programDto, user_name);
            log.debug("Create program is successful. New program is: {}", savedProgram);
            log.info("Create program is successful. New program is: {}", savedProgram);
            return ResponseEntity.ok(savedProgram);

        } catch (IllegalArgumentException e) {
            log.error("Error creating program: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            // Handle generic exceptions
            log.error("An error occurred while processing the request: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

    }


    //get list program with filter or not
    //http://localhost:9292/programs/?status=done&end_time=2021-08-01&program_name=program1&page=1&size=10
    @Operation(summary = "Get Programs", description = "Get all programs or filter by status, end time, program name")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Programs Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Body")
    })
    @GetMapping()
    public ResponseEntity<?> getProgramsByFilters(
            @RequestHeader(name = "user_name") String userName,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "end_time", required = false) LocalDate endTime,
            @RequestParam(name = "program_name", required = false) String programName,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            HttpServletRequest request
    ) {
        Pageable pageable = PageRequest.of(page - 1, size);
        try {
            log.debug("Received request to get programs by filters");
            log.debug("Filter Criteria - User: {}, Status: {}, End Time: {}, Program Name: {}", userName, status, endTime, programName);

            Page<ProgramResponse> programsList = programService.findByUserAndFilters(userName, status, endTime, programName, pageable);
            int totalPage = programsList.getTotalPages();
            int totalElements = (int) programsList.getTotalElements();
            List<ProgramResponse> programs = programsList.getContent();

            String rqID = (String) request.getAttribute("requestId");
            log.debug("Request ID: " + rqID + " List program: {}", programs);
            log.info("Request ID: " + rqID + " List program: {}", programs);

            return ResponseEntity.ok(ProgramListResponse.builder()
                    .programResponseList(programs)
                    .totalPage(totalPage)
                    .total(totalElements)
                    .build());
        } catch (Exception e) {
            log.error("An error occurred while processing the request: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }
    }

    //delete program by id
    @Operation(summary = "Delete Programs", description = "Delete Programs by programs ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete Program Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ProgramID"),
            @ApiResponse(responseCode = "404", description = "Program Not Found")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteProgram(@PathVariable int id) {
        if (id < 1) {
            log.debug("Program ID must be greater than 0");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program ID must be greater than 0");
        }
        try {
            ProgramDto findProgram = programService.findByProgramId(id);
            if (findProgram == null) {
                log.debug("Program not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Program not found");
            } else {


                ProgramDto program = programService.deleteById(id);
                log.debug("delete " + "{}" + " successfully", findProgram.getName());
                log.info("delete " + "{}" + " successfully", findProgram.getName());
                return ResponseEntity.status(HttpStatus.OK).body(findProgram.getName() + " deleted successfully");

            }
        } catch (Exception e) {
            log.error("An error occurred while processing the request: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
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
