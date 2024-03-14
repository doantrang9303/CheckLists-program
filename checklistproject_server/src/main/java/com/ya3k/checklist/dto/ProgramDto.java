package com.ya3k.checklist.dto;

import com.ya3k.checklist.entity.Users;
import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProgramDto {
    private int id;
    private String name;
    private  int user_id;
    private String status;
    private LocalDateTime create_time;
    private Date end_time;

}
