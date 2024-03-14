package com.ya3k.checklist.controller;

import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import org.apache.catalina.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.sql.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/programs")
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
        program.setCreateTime(LocalDateTime.now());
        Program savedProgram = repo.save(program);
        // Optional<Program> p = repo.findById(Integer.valueOf(String.valueOf(id)));
      //  Program curP = p.get();
        return ResponseEntity.ok(savedProgram);
    }

    //delete program by id
    @DeleteMapping("/delete")
    public ResponseEntity createProgram(@RequestParam Integer id)  {
        Optional<Program> p = repo.findById(id);
        Program curP = p.get();
        repo.delete(curP);
        return ResponseEntity.ok().body("Delete successfull");
    }

        //update program with id
    @PutMapping("/update")
    public ResponseEntity createProgram(@RequestBody  Program program,@RequestParam Integer id)  {
        Optional<Program> p = repo.findById(id);
        if (p ==null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("This id not exist!");
        }
        Program curP = p.get();
        curP.setStatus(program.getStatus());
        curP.setName(program.getName());
        repo.save(curP);
        return ResponseEntity.ok().body("Update successfull");
    }

    //get program by id
    @GetMapping("/get/{id}")
    public ResponseEntity getAllProgramsByUserId(@PathVariable Integer id){
        Users user = urepo.findByUserId(id);
        if (user==null ){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("id is not found.");
        }
        List<Program> list = repo.findByUserUserId(id);
        for (Program p : list){
            p.setUser(user);
        }
        return ResponseEntity.ok().body(list);

    }

    @GetMapping()
    public ResponseEntity getAllProgramsByUserId(
                                                 @RequestParam(name = "status", required = false) String status,
                                                 @RequestParam(name = "userName", required = true) String userName,
                                                 @RequestParam(name = "startDate", required = false) String startDate,
                                                 @RequestParam(name = "endDate", required = false) String endDate,
                                                 @RequestParam(name = "page", defaultValue = "1") int page,
                                                 @RequestParam(name = "size", defaultValue = "10") int size
                                                 ){
        Pageable pageRequest = PageRequest.of(page-1,size);
        if (status ==null){
            status = "";
        }
        if (startDate==null || startDate ==""){

            startDate = "1990-01-01";
        }
        if (endDate==null || endDate ==""){

            endDate = "2990-01-01";
        }
        if ( userName.isEmpty()){
           return  ResponseEntity.status(HttpStatus.BAD_REQUEST).body("This userName invalid!");
        }
        return ResponseEntity.ok().body(repo.findByStatusContainingIgnoreCaseAndCreateTimeBetweenAndUserUserName(status,Date.valueOf(startDate).toLocalDate().atStartOfDay(), Date.valueOf(endDate).toLocalDate().atStartOfDay(),userName,pageRequest));
    }



    


}
