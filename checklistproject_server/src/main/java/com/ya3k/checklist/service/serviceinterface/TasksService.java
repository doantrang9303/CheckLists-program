package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.response.programresponse.ProgramResponse;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Date;

public interface TasksService {

    Page<TasksResponse> listTasksOfProgram(int programId, Pageable pageable);

    Page<TasksResponse> findByProgramIdAndFilter(int programId, String status, String taskName, String endTime, Pageable pageable);
}
