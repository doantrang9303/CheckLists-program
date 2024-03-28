package com.ya3k.checklist.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Program;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.PastOrPresent;
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
    @Size(min = 3, max = 50, message = "Task name must be between 3 and 50 characters.")
    private String taskName;
    @JsonProperty("program_id")
    @Schema(hidden = true, description = "Program ID")
    private int programId;
    @JsonProperty("status")
    @Pattern(regexp = "^(IN_PROGRESS|COMPLETED|MISS_DEADLINE)$", message = "Status must be either IN_PROGRESS or COMPLETED")
    private String status;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @PastOrPresent(message = "end_time must be in the present or past.")
    @JsonProperty("end_time")
    private LocalDate endTime;
}
