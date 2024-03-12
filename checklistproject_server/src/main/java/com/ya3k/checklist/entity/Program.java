package com.ya3k.checklist.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Entity
@Table(name = "program")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Program {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int pId;
    @Column(name = "name")
    private String name;
    @Column(name="user_id")
    private int uId;
    @Column(name="status")
    private String sId;
    @Column(name="create_time")
    private Date cTime;
    @Column(name="end_time")
    private Date eTime;



}
