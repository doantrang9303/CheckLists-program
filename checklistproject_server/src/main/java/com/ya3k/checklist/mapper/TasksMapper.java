package com.ya3k.checklist.mapper;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Tasks;

public class TasksMapper {

    public static TasksDto tasksToDto(Tasks tasks) {
        TasksDto tasksDto = new TasksDto();
        tasksDto.setId(tasks.getId());
        tasksDto.setTaskName(tasks.getTaskName());
        tasksDto.setProgramId(tasks.getProgram().getId()); // Assuming Program has an 'id' field
        tasksDto.setStatus(tasks.getStatus());
        tasksDto.setCreateTime(tasks.getCreateTime());
        tasksDto.setEndTime(tasks.getEndTime());
        return tasksDto;
    }


    public static Tasks mapToTasks(TasksDto tasksDto) {
        Tasks tasks = new Tasks();
        tasks.setId(tasksDto.getId());
        tasks.setTaskName(tasksDto.getTaskName());
        // Assuming there's a separate service to fetch Program by ID and set it to the tasks object
//        tasks.setProgram(programService.findById(tasksDto.getProgramId()
        tasks.setStatus(tasksDto.getStatus());
        tasks.setCreateTime(tasksDto.getCreateTime());
        tasks.setEndTime(tasksDto.getEndTime());
        return tasks;
    }

}
