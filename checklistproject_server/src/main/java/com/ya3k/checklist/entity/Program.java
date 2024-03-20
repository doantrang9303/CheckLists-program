package com.ya3k.checklist.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "programs")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
  
    @Column(name = "name")
    @Size(min = 3, max = 50, message = "Program name must be between 3 and 50 characters.")
    private String name;
  
    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "user_id")
    private Users user;
  
    @Column(name="status")
    private String status;
  
    @Column(name="create_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @CreatedDate
    private LocalDateTime create_time;

    @Column(name="end_time")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonProperty("endtime")
    private LocalDate endTime;

    @OneToMany(mappedBy = "program", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonManagedReference
    private List<Tasks> listTask;

}
