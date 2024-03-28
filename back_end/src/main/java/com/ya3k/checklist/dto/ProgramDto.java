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

public class ProgramDto {


    @Schema(hidden = true)
    private int id;
    @JsonProperty("name")
    @Size(min = 3, max = 50, message = "Program name must be between 3 and 50 characters.")
    private String name;
    @JsonProperty("username")
    private String userName;
    @JsonProperty("status")
    private String status;
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;
    @JsonProperty("endtime")
    @FutureOrPresent(message = "End time must be in the present or future.")
    @NotNull(message = "endtime cannot be empty.")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endTime;


}
