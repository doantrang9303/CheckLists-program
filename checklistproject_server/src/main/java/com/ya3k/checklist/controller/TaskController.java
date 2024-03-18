package com.ya3k.checklist.controller;


import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;


@RestController
@RequestMapping("/task")
@CrossOrigin(origins = "${front-end.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TaskController {
    TaskRepository trepo;
    ProgramRepository repo;
    @Autowired
    public TaskController(ProgramRepository repo, TaskRepository trepo) {
        this.repo = repo;
        this.trepo = trepo;

    }

    @PostMapping("/add")
    public ResponseEntity<?> createProgram(@RequestBody Tasks task, @RequestHeader Integer program_id) {
        if(program_id<=0){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("program_id is invalid");
        }
        Program program=repo.findById(program_id).get();

        task.setProgram(program);
        if (task.getStatus() == null || task.getStatus().equals(""))
            task.setStatus("IN_PROGRESS");
        else task.setStatus(task.getStatus());
        task.setCreateTime(LocalDate.now());
        Tasks savedTask = trepo.save(task);
        return ResponseEntity.ok(savedTask);
    }


}
