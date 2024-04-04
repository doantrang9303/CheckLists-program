package com.ya3k.checklist.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
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
    @NotNull(message = "task_name is required.")
    @NotBlank(message = "Task name cannot be empty.")
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
    @CreatedDate
    private LocalDateTime createTime;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "end_time is required.")
    @FutureOrPresent(message = "end_time must be in the present or future.")
    @JsonProperty("end_time")
    private LocalDate endTime;
}
