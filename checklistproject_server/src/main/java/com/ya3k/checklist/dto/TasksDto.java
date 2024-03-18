package com.ya3k.checklist.dto;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.ya3k.checklist.entity.Program;
import jakarta.persistence.*;
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
    private String taskName;
    private int programId;
    private String status;
    private LocalDateTime createTime;
    private LocalDate endTime;
}
