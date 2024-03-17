package com.ya3k.checklist.controller;

import com.ya3k.checklist.response.taskresponse.TasksListResponse;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "${front-end.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TasksController {
    private final TasksService tasksService;

    @Autowired
    public TasksController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    //list tasks of program
    //http://localhost:9292/tasks/{program_id}?page=1&size=10
    /**
     * Retrieves a paginated list of tasks for a given program ID.
     *
     * @param programId The ID of the program to list tasks for.
     * @param page      The page number for pagination (default is 1).
     * @param size      The page size for pagination (default is 10).
     * @return ResponseEntity representing the paginated list of tasks and metadata.
     */
    @GetMapping("/{program_id}")
    public ResponseEntity<?> listTasksOfProgram(@PathVariable(name = "program_id") int programId,
                                                @RequestParam(name = "page", defaultValue = "1") int page,
                                                @RequestParam(name = "size", defaultValue = "10") int size,
                                                HttpSession session

    ) {
        try {
            if (page < 1 || size < 1) {
                page = 1;
                size = 10;
            }
            Pageable pageable = PageRequest.of(page - 1, size);

            if (programId < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program ID must be greater than 0");
            }

            session.setAttribute("program_id", programId);
            Page<TasksResponse> tasksList = tasksService.listTasksOfProgram(programId, pageable);

            if (tasksList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No tasks found");
            }
            //set program_id to request attribute

            int totalPages = tasksList.getTotalPages();
            int totalElements = (int) tasksList.getTotalElements();

            List<TasksResponse> tasks = tasksList.getContent();

            //set for filter

            return ResponseEntity.ok(TasksListResponse.builder()
                    .tasksResponseList(tasks)
                    .totalPage(totalPages)
                    .total(totalElements)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


    //filter tasks of program
    //http://localhost:9292/tasks/filter/{program_id}/?status=done&task_name=task1&end_time=2021-08-01&page=1&size=10


    @GetMapping("/filter/{program_id}")
    public ResponseEntity<?> filterTasksOfProgram(
            @PathVariable(name = "program_id") int programId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "task_name", required = false) String taskName,
            @RequestParam(name = "end_time", required = false) String endTime,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size,
            HttpSession session
    ) {
        try {
            if (page < 1 || size < 1) {
                page = 1;
                size = 10;
            }
            Pageable pageable = PageRequest.of(page - 1, size);

//            int programId = (int) session.getAttribute("program_id");
            if (programId < 1) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program ID must be greater than 0");
            }

            Page<TasksResponse> tasksList = tasksService.findByProgramIdAndFilter(programId, status, taskName, endTime, pageable);

            if (tasksList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No tasks found");
            }

            int totalPages = tasksList.getTotalPages();
            int totalElements = (int) tasksList.getTotalElements();

            List<TasksResponse> tasks = tasksList.getContent();

            //set for filter
            return ResponseEntity.ok(TasksListResponse.builder()
                    .tasksResponseList(tasks)
                    .totalPage(totalPages)
                    .total(totalElements)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }




}
