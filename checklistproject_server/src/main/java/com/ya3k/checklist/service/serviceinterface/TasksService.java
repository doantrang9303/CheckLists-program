package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.response.programresponse.ProgramResponse;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.Date;

public interface TasksService {

    Page<TasksResponse> listTasksOfProgram(int programId, Pageable pageable);

    Page<TasksResponse> findByProgramIdAndFilter(int programId, String status, String taskName, LocalDate endTime, Pageable pageable);

TasksDto deleteById(int id);
TasksDto findByTaskId(int id);

    }
