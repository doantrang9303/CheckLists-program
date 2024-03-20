package com.ya3k.checklist.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Program;
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


    private int id;
    @JsonProperty("taskName")
    private String taskName;
    @JsonProperty("program_id")
    private int programId;
    @JsonProperty("status")
    private String status;
    @JsonProperty("create_time")
    private LocalDateTime createTime;
    @JsonProperty("endTime")
    private LocalDate endTime;
}
