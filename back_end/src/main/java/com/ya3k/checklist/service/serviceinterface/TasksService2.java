package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.TasksDto;

public interface TasksService2 {
    TasksDto createTask(TasksDto taskDto, Integer programId);

}
