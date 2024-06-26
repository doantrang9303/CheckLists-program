package com.ya3k.checklist.controller;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.dto.response.taskresponse.ImportResponse;
import com.ya3k.checklist.dto.response.taskresponse.TasksListResponse;
import com.ya3k.checklist.dto.response.taskresponse.TasksResponse;
import com.ya3k.checklist.enumm.TasksApiNoti;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/tasks")
@CrossOrigin(origins = "${frontend.url}",
        allowedHeaders = "*",
        methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
@Tag(name = "Tasks API", description = "APIs for Tasks")
public class TasksController {

    private final TasksService tasksService;
    private final ProgramService programservice;

    public TasksController(TasksService tasksService, ProgramService programservice) {
        this.tasksService = tasksService;
        this.programservice = programservice;
    }


    //document
    @Operation(summary = "Add New Tasks", description = "Add New Tasks")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Add New Tasks Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Body"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })

        @PostMapping("/add")
    public ResponseEntity<String> createTask(@Valid @RequestBody TasksDto taskDto, @RequestHeader(name = "program_id") Integer programId) {
        log.debug("Received request to create a new task");
        try {
            if (programId < 1) {
                log.error(TasksApiNoti.PROGRAMIDNOTVALID.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TasksApiNoti.PROGRAMIDNOTVALID.getMessage());
            }

            TasksDto createdTask = tasksService.createTask(taskDto, programId);
            //log
            log.debug(TasksApiNoti.CREATETASKSUCCESS.getMessage(), createdTask);
            log.info(TasksApiNoti.CREATETASKSUCCESS.getMessage(), createdTask);

            programservice.autoUpdateStatusByTaskStatus(createdTask.getId());
            //log
            log.debug(TasksApiNoti.UPDATESTATUSSUCCESS.getMessage());
            log.info(TasksApiNoti.UPDATESTATUSSUCCESS.getMessage());
            return ResponseEntity.ok("Create task is successful. New task is: " + createdTask);
        } catch (IllegalArgumentException e) {
            log.error("Invalid argument: " + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    //filter tasks of program
    //http://localhost:9292/tasks/filter/{program_id}/?status=done&task_name=task1&end_time=2021-08-01&page=1&size=10

    @Operation(summary = "Get Tasks", description = "Get all Tasks of programs with program ID and filter by status, task name, end time")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Get Tasks Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Body"),
            @ApiResponse(responseCode = "404", description = "Tasks Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
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
            log.debug("Received request to get tasks of program with program ID: " + programId);
            if (page < 1 || size < 1) {
                page = 1;
                size = 10;
            }
            Pageable pageable = PageRequest.of(page - 1, size);

            if (programId < 1) {
                log.error(TasksApiNoti.PROGRAMIDNOTVALID.getMessage());
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TasksApiNoti.PROGRAMIDNOTVALID.getMessage());
            }

            Page<TasksResponse> tasksList = tasksService.findByProgramIdAndFilter(programId, status, taskName, endTime, pageable);

            int totalPages = tasksList.getTotalPages();
            int totalElements = (int) tasksList.getTotalElements();

            List<TasksResponse> tasks = tasksList.getContent();
            log.info("Program " + "{} " + "have list task: " + "{}", programId, tasks);
            log.debug("Program " + "{} " + "have list task: " + "{}", programId, tasks);
            return ResponseEntity.ok(TasksListResponse.builder()
                    .tasksResponseList(tasks)
                    .totalPage(totalPages)
                    .total(totalElements)
                    .build());

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

    }

    @Operation(summary = "Delete Tasks", description = "Delete Tasks by Tasks ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Delete Tasks Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Tasks ID"),
            @ApiResponse(responseCode = "404", description = "Tasks Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteTask(@PathVariable int id) {

        if (id < 1) {
            log.error(TasksApiNoti.TASKIDNOTVALID.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(TasksApiNoti.TASKIDNOTVALID.getMessage());
        }
        try {
            log.debug("Received request to delete task with task ID: " + id);
            log.info("Received request to delete task with task ID: " + id);
            TasksDto findTask = tasksService.findByTaskId(id);
            if (findTask == null) {
                log.debug(TasksApiNoti.TASKNOTFOUND.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Task not found");
            } else {
                tasksService.deleteById(id);
                log.debug("Delete " + "{}" + " successfull", findTask.getTaskName());
                log.info("Delete " + "{}" + " successfull", findTask.getTaskName());
                return ResponseEntity.status(HttpStatus.OK).body(findTask.getTaskName() + " deleted successfully");

            }

        } catch (Exception e) {
            log.error(TasksApiNoti.REQUESTERROR.getMessage() + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
        }

    }

//    @PostMapping("/importTasksFromExcel")
//    public ResponseEntity<ImportResponse> importTasksFromExcel(@RequestParam(name = "program_id") int programId, @RequestParam("file") MultipartFile filePath) throws IOException {
//        log.debug("Received request to import tasks from excel file");
//        log.info("Received request to import tasks from excel file");
//        ImportResponse response = tasksService.inportTask(filePath, programId);
//        log.debug("Import tasks from excel file successfully");
//        log.info("Import tasks from excel file successfully");
//        return ResponseEntity.ok().body(response);
//    }


    @Operation(summary = "Update Tasks", description = "Update Tasks by Tasks ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Update Tasks Successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid Tasks ID"),
            @ApiResponse(responseCode = "404", description = "Tasks Not Found"),
            @ApiResponse(responseCode = "500", description = "Internal Server Error")
    })
    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateTask(@PathVariable int id, @RequestBody @Valid TasksDto updatedTaskDto) {
        try {
            if (id < 1) {
                log.error(TasksApiNoti.TASKIDNOTVALID.getMessage());
                throw new IllegalArgumentException("Task ID must be greater than 0");
            }

            TasksDto findTask = tasksService.findByTaskId(id);
            if (findTask == null) {
                log.error(TasksApiNoti.TASKNOTFOUND.getMessage());
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(TasksApiNoti.TASKNOTFOUND.getMessage());
            }

            tasksService.updateTask(id, updatedTaskDto);
            String updateMessage = generateUpdateMessage(findTask, updatedTaskDto);
            log.info("Task updated: {}", updateMessage);
            log.debug("Task updated: {}", updateMessage);
            return ResponseEntity.ok(updateMessage);

        } catch (IllegalArgumentException | EntityNotFoundException e) {
            log.error(TasksApiNoti.REQUESTERROR.getMessage() + e.getMessage(), e);
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            log.error(TasksApiNoti.REQUESTERROR.getMessage() + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    private String generateUpdateMessage(TasksDto findTask, TasksDto updatedTaskDto) {
        StringBuilder updateMessage = new StringBuilder();

        if (!Objects.equals(findTask.getTaskName(), updatedTaskDto.getTaskName())) {
            appendUpdateMessage(updateMessage, "Task Name", findTask.getTaskName(), updatedTaskDto.getTaskName());
        }
        if (!Objects.equals(findTask.getStatus(), updatedTaskDto.getStatus())) {
            appendUpdateMessage(updateMessage, "Status", findTask.getStatus(), updatedTaskDto.getStatus());
        }
        if (!Objects.equals(findTask.getEndTime(), updatedTaskDto.getEndTime())) {
            appendUpdateMessage(updateMessage, "End Time", findTask.getEndTime(), updatedTaskDto.getEndTime());
        }

        return updateMessage.length() > 0 ? updateMessage.toString() : "No changes were made to the task";
    }

    private void appendUpdateMessage(StringBuilder builder, String label, Object oldValue, Object newValue) {
        builder.append(label).append(" updated: ").append(oldValue).append(" to ").append(newValue).append(".\n");
    }

    @PostMapping("/testUpload")
    public ResponseEntity<ImportResponse> handleUpload(@RequestParam(name = "program_id") int programId, @RequestParam(name = "file") MultipartFile file) throws IOException {
        log.debug("Received request to import tasks from excel file");
        log.info("Received request to import tasks from excel file");
        ImportResponse response = tasksService.hanldeUloadFile(programId, file);
        log.debug("Import tasks from excel file successfully");
        log.info("Import tasks from excel file successfully");
        return ResponseEntity.ok().body(response);
    }
}
