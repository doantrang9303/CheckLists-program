package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.Enum.StatusEnum;
import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.mapper.TasksMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.dto.response.taskresponse.TasksResponse;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TasksService {
    private final TasksRepository tasksRepository;
    private final ProgramRepository programRepository;

    private final ApplicationEventPublisher eventPublisher;
    private final ProgramService programService;

    @Autowired
    public TaskServiceImpl(TasksRepository tasksRepository, ProgramRepository programRepository, ApplicationEventPublisher eventPublisher, ProgramService programService) {
        this.programRepository = programRepository;
        this.tasksRepository = tasksRepository;
        this.eventPublisher = eventPublisher;
        this.programService = programService;
    }

    @Override
    public TasksDto createTask(TasksDto taskDto, Integer programId) {
        Program programs = programRepository.findById(programId).orElseThrow(() -> new EntityNotFoundException("Program not found"));
        List<String> errorsMess = new ArrayList<>();

        if (programs != null) {
            Tasks tasks = TasksMapper.mapToTasks(taskDto);

            //validate task name
            if (taskDto.getTaskName() != null) {
                String trimmedName = taskDto.getTaskName().trim();
                if (trimmedName.isEmpty() || trimmedName.isBlank()) {
                    errorsMess.add("Task name is required");
                } else if (trimmedName.length() < 3 || trimmedName.length() > 50) {
                    errorsMess.add("Task name must be between 3 and 50 characters.");
                } else {
                    //set task name
                    tasks.setTaskName(trimmedName);
                }
            } else {
                errorsMess.add("Task name is required");
            }

            //set program
            tasks.setProgram(programs);

            //set default status to IN_PROGRESS
            if (tasks.getStatus() == null || tasks.getStatus().isEmpty() || tasks.getStatus().isBlank()) {
                tasks.setStatus(StatusEnum.IN_PROGRESS.getStatus());
            }

            //set create time
            tasks.setCreateTime(LocalDateTime.now());

            if (taskDto.getEndTime() != null) {
                if (tasks.getEndTime().isBefore(LocalDate.now())) {
                    errorsMess.add("End time must be in the future");
                } else if (tasks.getEndTime().isAfter(programs.getEndTime())) {
                    errorsMess.add("Tasks End time must be before program end time");
                }else {
                    tasks.setEndTime(taskDto.getEndTime());
                }
            }


            //print error message
            if (!errorsMess.isEmpty()) {
                throw new IllegalArgumentException(String.join("\n", errorsMess));
            }
            tasksRepository.save(tasks);
            return TasksMapper.tasksToDto(tasks);
        }

        return null;
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


    @Override
    public TasksDto updateTask(Integer taskId, TasksDto updatedTaskDto) {

//        Tasks tasks = tasksRepository.findByTasksId(taskId);
        Tasks tasks = tasksRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        List<String> errorsMess = new ArrayList<>();

        if (tasks != null) {
            // Update name with trimmed spaces and validate length
            if (updatedTaskDto.getTaskName() != null) {
                String trimmedName = updatedTaskDto.getTaskName().trim();
                if (trimmedName.isEmpty() || trimmedName.isBlank()) {
                    tasks.setTaskName(tasks.getTaskName());
                } else if (trimmedName.length() < 3 || trimmedName.length() > 50) {
                    errorsMess.add("Task name must be between 3 and 50 characters.");
                } else {
                    tasks.setTaskName(trimmedName);
                }
            } else {
                tasks.setTaskName(tasks.getTaskName());
            }
            //update status
            if (updatedTaskDto.getStatus() != null &&
                    !updatedTaskDto.getStatus().isEmpty() &&
                    !updatedTaskDto.getStatus().isBlank()) {
                String status = updatedTaskDto.getStatus().toUpperCase();
                if (StatusEnum.IN_PROGRESS.toString().equals(status) || StatusEnum.COMPLETED.toString().equals(status)) {
                    tasks.setStatus(status);
                } else {
                    errorsMess.add("Status must be IN_PROGRESS or COMPLETED");
                }
            }

            //update end time
            if (updatedTaskDto.getEndTime() != null) {
                String endTimeString = updatedTaskDto.getEndTime().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate endTime = LocalDate.parse(endTimeString, formatter);
                    if (endTime.isBefore(LocalDate.now())) {
                        errorsMess.add("End time must be in the future");
                    } else if (endTime.isAfter(tasks.getProgram().getEndTime())) {
                        errorsMess.add("Tasks End time must be before program end time");
                    }

                    tasks.setEndTime(endTime);
                } catch (DateTimeParseException e) {
                    errorsMess.add("End time must be in the format YYYY-MM-DD");
                }
            }

            //print error message
            if (!errorsMess.isEmpty()) {
                throw new IllegalArgumentException(String.join("\n", errorsMess));
            }

            programService.autoUpdateStatusByTaskStatus(taskId);

            tasksRepository.save(tasks);
            return TasksMapper.tasksToDto(tasks);
        }
        return null;
    }

    public void inportTask(MultipartFile file, int programId) {
        try {

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
                    if (currentRow.getCell(0) ==null){
                        continue;
                    }

                    Cell taskNameCell = currentRow.getCell(1);
                    task.setTaskName(taskNameCell.getStringCellValue());

                    Program program = new Program();
                    program.setId(programId);
                    task.setProgram(program);

                    Cell statusCell = currentRow.getCell(2);
                    task.setStatus(statusCell.getStringCellValue());

                    Cell createTimeCell = currentRow.getCell(3);
                    LocalDateTime createTime = LocalDateTime.parse(createTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    task.setCreateTime(createTime);

                    Cell endTimeCell = currentRow.getCell(4);
                    LocalDate endTime = LocalDate.parse(endTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    task.setEndTime(endTime);

                    tasksRepository.save(task);
                }

                workbook.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

}
