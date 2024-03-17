package com.ya3k.checklist.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Data

public class Tasks {
    @Id
    private int id;

    @Column(name = "name")
    private String taskName;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "program_id")
    private Program program;

    @Column(name = "status")
    private String status;

    @Column(name = "create_time")
    @CreatedDate
    private LocalDate createTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Column(name = "end_time")
    private Date endTime;


}
