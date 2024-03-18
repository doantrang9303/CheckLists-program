package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.response.taskresponse.TasksListResponse;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.cglib.core.Local;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "${front-end.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
public class TasksController {
    private final TasksService tasksService;

    private TasksRepository trepo;
    private ProgramRepository repo;

    @Autowired
    public TasksController(TasksService tasksService, TasksRepository trepo, ProgramRepository repo) {
        this.tasksService = tasksService;
        this.trepo = trepo;
        this.repo = repo;
    }


    @PostMapping("/add")
    public ResponseEntity<?> createProgram(@RequestBody Tasks task, @RequestHeader Integer program_id) {
        if (program_id <= 0) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("program_id is invalid");
        }
        Program program = repo.findById(program_id).get();

        if (program == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Program not found");
        }
        task.setProgram(program);
        if (task.getStatus() == null || task.getStatus().equals(""))
            task.setStatus("IN_PROGRESS");
        else task.setStatus(task.getStatus());
        task.setCreateTime(LocalDateTime.now());
        Tasks savedTask = trepo.save(task);
        return ResponseEntity.ok(savedTask.getTaskName() + " add successfully");
    }


    //filter tasks of program
    //http://localhost:9292/tasks/filter/{program_id}/?status=done&task_name=task1&end_time=2021-08-01&page=1&size=10

    /**
     * Retrieves a paginated list of tasks for a given program ID.
     * <p>
     * //     * @param programId The ID of the program to list tasks for.
     *
     * @param status   The status of the tasks to filter by (optional).
     * @param taskName The name of the tasks to filter by (optional).
     * @param endTime  The end time of the tasks to filter by (optional).
     * @param page     The page number for pagination (default is 1).
     * @param size     The page size for pagination (default is 10).
     * @return ResponseEntity representing the paginated list of tasks and metadata.
     */
    @GetMapping("/{program_id}")
    public ResponseEntity<?> filterTasksOfProgram(
            @PathVariable(name = "program_id") int programId,
            @RequestParam(name = "status", required = false) String status,
            @RequestParam(name = "task_name", required = false) String taskName,
            @RequestParam(name = "end_time", required = false) LocalDate endTime,
            @RequestParam(name = "page", defaultValue = "1") int page,
            @RequestParam(name = "size", defaultValue = "10") int size
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

            return ResponseEntity.ok(TasksListResponse.builder()
                    .tasksResponseList(tasks)
                    .totalPage(totalPages)
                    .total(totalElements)
                    .build());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable int id) {
        if (id < 1) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Task ID must be greater than 0");
        }
        try {
            TasksDto findTask = tasksService.findByTaskId(id);
            if (findTask == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            } else {
                TasksDto task = tasksService.deleteById(id);
                return ResponseEntity.status(HttpStatus.OK).body(findTask.getTaskName() + " deleted successfully");

            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }


}
