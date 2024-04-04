package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.enumm.StatusEnum;
import com.ya3k.checklist.mapper.TasksMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.service.serviceinterface.TasksService2;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;


import static com.ya3k.checklist.mapper.TasksMapper.mapToTasks;

@Service
public class TasksServices2 implements TasksService2 {
    private final static String DATETIMEPATTERN = "yyyy-MM-dd";
    private final TasksRepository tasksRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public TasksServices2(TasksRepository tasksRepository, ProgramRepository programRepository, ProgramService programService) {
        this.programRepository = programRepository;
        this.tasksRepository = tasksRepository;
    }


    @Override
    public TasksDto createTask(TasksDto taskDto, Integer programId) {
        Program program = programRepository.findById(programId).orElseThrow(() -> new EntityNotFoundException("Program not found"));
        Tasks task = mapToTasks(taskDto);

        if (taskDto.getTaskName() != null) {
            String trimmedName = taskDto.getTaskName().trim();
            task.setTaskName(trimmedName);
        }
        task.setProgram(program);

        task.setStatus(StatusEnum.IN_PROGRESS.name());

        task.setCreateTime(LocalDateTime.now());

        if (taskDto.getEndTime() != null) {
            String endTimeString = taskDto.getEndTime().toString();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATETIMEPATTERN);
            try {
                LocalDate endTime = LocalDate.parse(endTimeString, formatter);
                task.setEndTime(endTime);
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Please use yyyy-MM-dd");
            }
        }
        tasksRepository.save(task);
        return TasksMapper.tasksToDto(task);
    }
}
