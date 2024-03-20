package com.ya3k.checklist.dto.response.taskresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Tasks;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TasksResponse {

    @JsonProperty("id")
    private int id;

    @JsonProperty("task_name")
    private String taskName;

    @JsonProperty("program_id")
    private int programId;

    @JsonProperty("status")
    private String status;

    @JsonProperty("create_time")
    private LocalDateTime createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("end_time")
    private LocalDate endTime;

    public static TasksResponse fromTasks(Tasks tasks) {
        TasksResponse tasksResponse = TasksResponse.builder()
                .id(tasks.getId())
                .taskName(tasks.getTaskName())
                .programId(tasks.getProgram().getId())
                .status(tasks.getStatus())
                .createTime(tasks.getCreateTime())
                .endTime(tasks.getEndTime())
                .build();
        return tasksResponse;
    }
}
