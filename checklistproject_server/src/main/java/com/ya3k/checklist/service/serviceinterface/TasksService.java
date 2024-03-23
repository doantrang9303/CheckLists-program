package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.dto.response.taskresponse.TasksResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface TasksService {

    TasksDto createTask(TasksDto taskDto, Integer programId);

    Page<TasksResponse> listTasksOfProgram(int programId, Pageable pageable);

    Page<TasksResponse> findByProgramIdAndFilter(int programId, String status, String taskName, LocalDate endTime, Pageable pageable);

    TasksDto deleteById(int id);

    TasksDto findByTaskId(int id);

    TasksDto updateTask(Integer taskId, TasksDto updatedTaskDto);

}
