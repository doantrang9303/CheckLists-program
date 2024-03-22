package com.ya3k.checklist.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Program;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TasksDto {

    @Schema(hidden = true, description = "Task ID")
    private int id;
    @JsonProperty("task_name")
    @Schema(description = "Task Name")
    private String taskName;
    @JsonProperty("program_id")
    @Schema(hidden = true, description = "Program ID")
    private int programId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @JsonProperty("end_time")
    private LocalDate endTime;
}
