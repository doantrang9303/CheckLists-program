package com.ya3k.checklist.service.serviceimpl;

import com.google.gson.Gson;
import com.ya3k.checklist.enumm.StatusEnum;
import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.dto.response.taskresponse.ImportResponse;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.mapper.TasksMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.dto.response.taskresponse.TasksResponse;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import com.ya3k.checklist.service.websocket.MessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

import static com.ya3k.checklist.mapper.TasksMapper.mapToTasks;


@Slf4j
@Service
public class TaskServiceImpl implements TasksService {
    private static String dateTimePattern = "yyyy-MM-dd";
    private final TasksRepository tasksRepository;
    private final ProgramRepository programRepository;

    @Autowired
    private SimpMessagingTemplate messageService;

    @Autowired
    public TaskServiceImpl(TasksRepository tasksRepository, ProgramRepository programRepository, ProgramService programService) {
        this.programRepository = programRepository;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public TasksDto createTask(TasksDto taskDto, Integer programId) {
        // Find the program by ID or throw an exception if not found
        Program program = programRepository.findById(programId)
                .orElseThrow(() -> new EntityNotFoundException("Program not found"));

        // Validate task name
        String taskName = taskDto.getTaskName();
        if (taskName == null || taskName.trim().isEmpty()) {
            throw new IllegalArgumentException("Task name is required");
        } else if (taskName.trim().length() < 3 || taskName.trim().length() > 50) {

            throw new IllegalArgumentException("Task name must be between 3 and 50 characters.");
        }

        // Create a new task
        Tasks task = TasksMapper.mapToTasks(taskDto);
        task.setTaskName(taskName.trim());
        task.setProgram(program);

        // Set default status to IN_PROGRESS if not provided
        if (task.getStatus() == null || task.getStatus().trim().isEmpty()) {
            task.setStatus(StatusEnum.IN_PROGRESS.getStatus());
        }

        // Set create time to current time
        task.setCreateTime(LocalDateTime.now());

        // Validate and set end time if provided
        LocalDate endTime = taskDto.getEndTime();
        if (endTime != null) {
            if (endTime.isBefore(LocalDate.now())) {
                throw new IllegalArgumentException("End time must be in the future");
            } else if (endTime.isAfter(program.getEndTime())) {
                throw new IllegalArgumentException("Task end time must be before program end time");
            }
            task.setEndTime(endTime);
        }

        tasksRepository.save(task);
        return TasksMapper.tasksToDto(task);
    }


    @Override
    public Page<TasksResponse> listTasksOfProgram(int programId, Pageable pageable) {
        Optional<Program> programs = programRepository.findById(programId);
        if (programs.isEmpty()) {
            throw new IllegalArgumentException("Program not existed");
        }

        Page<Tasks> tasks = tasksRepository.listTasksOfProgram(programId, pageable);

        return tasks.map(TasksResponse::fromTasks);
    }

    @Override
    public TasksDto updateTask(Integer taskId, TasksDto updatedTaskDto) {
        Tasks tasks = tasksRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found"));
        List<String> errorsMess = new ArrayList<>();

        // Update task name
        String updatedName = updatedTaskDto.getTaskName();
        if (updatedName != null) {
            String trimmedName = updatedName.trim();
            if (!trimmedName.isEmpty() && trimmedName.length() >= 3 && trimmedName.length() <= 50) {
                tasks.setTaskName(trimmedName);
            } else {
                errorsMess.add("Task name must be between 3 and 50 characters.");
            }
        }

        // Update status if provided and valid
        String updatedStatus = updatedTaskDto.getStatus();
        if (updatedStatus.equals(StatusEnum.IN_PROGRESS.getStatus()) || updatedStatus.equals(StatusEnum.COMPLETED.getStatus()) || updatedStatus.equals(StatusEnum.MISS_DEADLINE.getStatus())) {
            tasks.setStatus(updatedStatus);
        } else {
            errorsMess.add("Status must be either IN_PROGRESS or COMPLETED");
        }
        // Update end time if provided and valid
        LocalDate updatedEndTime = updatedTaskDto.getEndTime();
        if (updatedEndTime != null) {
            if (updatedEndTime.isBefore(LocalDate.now())) {
                errorsMess.add("End time must be in the future");
            } else if (tasks.getProgram() != null && updatedEndTime.isAfter(tasks.getProgram().getEndTime())) {
                errorsMess.add("Task end time must be before program end time");
            } else {
                tasks.setEndTime(updatedEndTime);
            }
        }

        // Print error messages if any
        if (!errorsMess.isEmpty()) {
            throw new IllegalArgumentException(String.join("\n", errorsMess));
        }

        // Save the updated task and return its DTO
        tasksRepository.save(tasks);
        return TasksMapper.tasksToDto(tasks);
    }


    @Override
    public Page<TasksResponse> findByProgramIdAndFilter(int programId, String status, String taskName, LocalDate endTime, Pageable pageable) {
        Optional<Program> programs = programRepository.findById(programId);

        if (programs.isEmpty()) {
            throw new IllegalArgumentException("Program not existed");
        }

        Page<Tasks> tasks = tasksRepository.findByProgramIdAndFilter(programId, status, taskName, endTime, pageable);

        return tasks.map(TasksResponse::fromTasks);
    }

    @Override
    public TasksDto deleteById(int id) {
        Tasks tasks = tasksRepository.deleteById(id);
        if (tasks != null) {
            return TasksMapper.tasksToDto(tasks);
        }
        return null;
    }

    @Override
    public TasksDto findByTaskId(int id) {
        Tasks tasks = tasksRepository.findByTasksId(id);
        if (tasks != null) {
            return TasksMapper.tasksToDto(tasks);
        }
        return null;
    }


    public ImportResponse inportTask(MultipartFile file, int programId) {
        try {
            String msg = "";
            int countAll = 0;
            int countSaved = 0;
            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();

                // Skip header row if needed
                if (iterator.hasNext()) {
                    iterator.next(); // Skip header row
                }
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    Tasks task = new Tasks();
                    Cell NoNumber = currentRow.getCell(0);
                    if (NoNumber == null) {
                        break;
                    }

                    countAll++;
                }

                iterator = datatypeSheet.iterator();

                // Skip header row if needed
                if (iterator.hasNext()) {
                    iterator.next(); // Skip header row
                }
                int count = 0;
                while (iterator.hasNext()) {
                    count++;
                    String subMsg = "";
                    Row currentRow = iterator.next();
                    Tasks task = new Tasks();
                    Cell NoNumber = currentRow.getCell(0);
                    if (NoNumber == null) {
                        continue;
                    }
                    Cell taskNameCell = currentRow.getCell(1);
                    task.setTaskName(taskNameCell.getStringCellValue());

                    Program program = programRepository.getById(Integer.valueOf(programId));
                    if (program == null) {
                        subMsg += "Row " + count + " is have error. This programId not exist!\n";

                    } else {
                        task.setProgram(program);
                    }

                    Cell statusCell = currentRow.getCell(2);
                    if (statusCell == null) {
                        task.setStatus("IN_PROGRESS");
                    } else {
                        if(statusCell.getStringCellValue() == ""){
                            task.setStatus("IN_PROGRESS");
                        }else {
                            if (statusCell.getStringCellValue().trim().equalsIgnoreCase("COMPLETED") || statusCell.getStringCellValue().trim().equalsIgnoreCase("IN_PROGRESS")) {
                                task.setStatus(statusCell.getStringCellValue());

                            } else {
                                subMsg += "Row " + count + " is have error. Status is invalid!\n";
                            }
                        }
                    }

                    Cell createTimeCell = currentRow.getCell(3);

                    if (createTimeCell == null) {
                        LocalDateTime createTime = LocalDateTime.now();
                        task.setCreateTime(createTime);

                    } else {
                        LocalDateTime createTime = LocalDateTime.now();
                        task.setCreateTime(createTime);
                        if (!createTimeCell.getStringCellValue().equals("")) {
                            createTime = LocalDateTime.parse(createTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            if (createTime.isBefore(LocalDateTime.now())) {
                                subMsg += "Row " + count + " is have error. Create time must be after today!\n";
                                task.setCreateTime(createTime);

                            } else {
                                task.setCreateTime(createTime);
                            }
                        }

                    }


                    Cell endTimeCell = currentRow.getCell(4);
                    if (endTimeCell == null) {

                        subMsg += "Row " + count + " is have error. Endtime not allow null!\n";
                    } else {

                        LocalDate endTime = LocalDate.parse(endTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern(dateTimePattern));
                        if (endTime.isBefore(task.getCreateTime().toLocalDate())) {
                            subMsg += "Row " + count + " is have error. Endtime not allow before create time!\n";
                        }
                        if (endTime.isAfter(program.getEndTime())) {
                            subMsg += "Row " + count + " is have error. Endtime of task not allow after Endtime of Program!\n";

                        }
                        task.setEndTime(endTime);

                    }
                    if (subMsg.isEmpty()) {
                        tasksRepository.save(task);
                        countSaved++;

                    }
                    msg += subMsg;
                }

                workbook.close();
            }
            msg = countAll == countSaved ? "Saved all rows successfully" : msg;
            return new ImportResponse(msg, countAll, countSaved);
        } catch (Exception e) {
            e.printStackTrace();
            return new ImportResponse(e.getMessage(), 0, 0);
        }

    }

    @Override
    public ImportResponse hanldeUloadFile(int programId, MultipartFile file) {
//        try {
//            for (int i = 0; i <= 100; i += 10) {
//                Thread.sleep(1000); // Simulate processing
//                messageService.convertAndSend("/topic/progress", new ImportResponse("Importing", i, 100));
//            }
//        } catch (InterruptedException e) {
//            return new ImportResponse(e.getMessage(), 0, 0);
//        }
//        return new ImportResponse("Done", 0, 0);
        try {
            String msg = "";
            int countAll = 0;
            int countSaved = 0;
            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();

                // Skip header row if needed
                if (iterator.hasNext()) {
                    iterator.next(); // Skip header row
                }
                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    Tasks task = new Tasks();
                    Cell NoNumber = currentRow.getCell(0);
                    if (NoNumber == null) {
                        break;
                    }

                    countAll++;
                }

                iterator = datatypeSheet.iterator();

                // Skip header row if needed
                if (iterator.hasNext()) {
                    iterator.next(); // Skip header row
                }
                int count = 0;
                while (iterator.hasNext()) {
                    count++;
                    String subMsg = "";
                    Row currentRow = iterator.next();
                    Tasks task = new Tasks();
                    Cell NoNumber = currentRow.getCell(0);
                    if (NoNumber == null) {
                        continue;
                    }
                    Cell taskNameCell = currentRow.getCell(1);
                    task.setTaskName(taskNameCell.getStringCellValue());

                    Program program = programRepository.getById(Integer.valueOf(programId));
                    if (program == null) {
                        subMsg += "Row " + count + " is have error. This programId not exist!\n";

                    } else {
                        task.setProgram(program);
                    }

                    Cell statusCell = currentRow.getCell(2);
                    if (statusCell == null) {
                        task.setStatus("IN_PROGRESS");
                    } else {
                        if(statusCell.getStringCellValue() == ""){
                            task.setStatus("IN_PROGRESS");
                        }else {
                            if (statusCell.getStringCellValue().trim().equalsIgnoreCase("COMPLETED") || statusCell.getStringCellValue().trim().equalsIgnoreCase("IN_PROGRESS")) {
                                task.setStatus(statusCell.getStringCellValue());

                            } else {
                                subMsg += "Row " + count + " is have error. Status is invalid!\n";
                            }
                        }
                    }

                    Cell createTimeCell = currentRow.getCell(3);

                    if (createTimeCell == null) {
                        LocalDateTime createTime = LocalDateTime.now();
                        task.setCreateTime(createTime);

                    } else {
                        LocalDateTime createTime = LocalDateTime.now();
                        task.setCreateTime(createTime);
                        if (!createTimeCell.getStringCellValue().equals("")) {
                            createTime = LocalDateTime.parse(createTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                            if (createTime.isBefore(LocalDateTime.now())) {
                                subMsg += "Row " + count + " is have error. Create time must be after today!\n";
                                task.setCreateTime(createTime);

                            } else {
                                task.setCreateTime(createTime);
                            }
                        }

                    }


                    Cell endTimeCell = currentRow.getCell(4);
                    if (endTimeCell == null) {

                        subMsg += "Row " + count + " is have error. Endtime not allow null!\n";
                    } else {

                        LocalDate endTime = LocalDate.parse(endTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern(dateTimePattern));
                        if (endTime.isBefore(task.getCreateTime().toLocalDate())) {
                            subMsg += "Row " + count + " is have error. Endtime not allow before create time!\n";
                        }
                        if (endTime.isAfter(program.getEndTime())) {
                            subMsg += "Row " + count + " is have error. Endtime of task not allow after Endtime of Program!\n";

                        }
                        task.setEndTime(endTime);

                    }
                    if (subMsg.isEmpty()) {
                        tasksRepository.save(task);
                        countSaved++;
                    }
                    msg += subMsg;
                    messageService.convertAndSend("/topic/progress", new ImportResponse(msg, countAll, countSaved));
                }

                workbook.close();
            }
            msg = countAll == countSaved ? "Saved all rows successfully" : msg;
            return new ImportResponse(msg, countAll, countSaved);
        } catch (Exception e) {
            e.printStackTrace();
            return new ImportResponse(e.getMessage(), 0, 0);
        }
    }

}
