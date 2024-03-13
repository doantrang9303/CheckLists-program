package com.ya3k.checklist.repository;

import com.ya3k.checklist.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;


import java.sql.Date;
import java.time.LocalDateTime;
import java.util.List;

public interface ProgramRepository extends JpaRepository<Program, Integer> {

    List<Program> findByUserUserId(Integer userID);

    Page<Program> findByStatusContainingIgnoreCaseAndCreateTimeBetween(String status, LocalDateTime startDate, LocalDateTime endDate, Pageable pageable);
}
