package com.ya3k.checklist.response.taskresponse;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

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
    private LocalDate createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("end_time")
    private Date endTime;

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
