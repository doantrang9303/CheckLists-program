package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.mapper.TasksMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;

@Service
public class TaskServiceImpl implements TasksService {
    private final TasksRepository tasksRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public TaskServiceImpl(TasksRepository tasksRepository, ProgramRepository programRepository) {
        this.programRepository = programRepository;
        this.tasksRepository = tasksRepository;
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


}
