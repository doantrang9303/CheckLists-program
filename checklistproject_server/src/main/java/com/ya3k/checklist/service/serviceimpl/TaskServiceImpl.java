package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.Enum.StatusEnum;
import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.event.eventhandle.ProgramEventHandle;
import com.ya3k.checklist.mapper.TasksMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TasksService {
    private final TasksRepository tasksRepository;
    private final ProgramRepository programRepository;

    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    public TaskServiceImpl(TasksRepository tasksRepository, ProgramRepository programRepository, ApplicationEventPublisher eventPublisher) {
        this.programRepository = programRepository;
        this.tasksRepository = tasksRepository;
        this.eventPublisher = eventPublisher;
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
        Tasks tasks= tasksRepository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("Task not found"));
        if (tasks != null) {

            //update name
            if (updatedTaskDto.getTaskName() != null &&
                    !updatedTaskDto.getTaskName().isEmpty() &&
                    !updatedTaskDto.getTaskName().isBlank()) {
                tasks.setTaskName(updatedTaskDto.getTaskName());
            }

            //update status
            if (updatedTaskDto.getStatus() != null &&
                    !updatedTaskDto.getStatus().isEmpty() &&
                    !updatedTaskDto.getStatus().isBlank()) {
                String status = updatedTaskDto.getStatus().toUpperCase();
                if (StatusEnum.IN_PROGRESS.toString().equals(status) || StatusEnum.COMPLETED.toString().equals(status)) {
                    tasks.setStatus(status);
                } else {
                    throw new IllegalArgumentException("Status must be IN_PROGRESS or COMPLETED");
                }
            }

            //update end time
            if (updatedTaskDto.getEndTime() != null) {
                String endTimeString = updatedTaskDto.getEndTime().toString();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                try {
                    LocalDate endTime = LocalDate.parse(endTimeString, formatter);
                    if (endTime.isBefore(LocalDate.now())) {
                        throw new IllegalArgumentException("End time must be in the future");
                    }
                    tasks.setEndTime(endTime);
                } catch (DateTimeParseException e) {
                    throw new IllegalArgumentException("End time must be in the format YYYY-MM-DD");
                }
            }

            //handler auto update status for program
            Program program = tasks.getProgram();
            List<Tasks> allTasks = program.getListTask();
            boolean allTasksCompleted = allTasks.stream().allMatch(t -> "COMPLETED".equals(t.getStatus()));

            // Update program status if all tasks are completed
            if (allTasksCompleted) {
                program.setStatus("COMPLETED");
                programRepository.save(program);
                // Publish program status change event
                eventPublisher.publishEvent(new ProgramEventHandle(this, program));
            }else{
                program.setStatus("IN_PROGRESS");
                programRepository.save(program);
                // Publish program status change event
                eventPublisher.publishEvent(new ProgramEventHandle(this, program));
            }


            tasksRepository.save(tasks);
            return TasksMapper.tasksToDto(tasks);
        }
        return null;
    }


}
