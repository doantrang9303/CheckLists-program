package com.ya3k.checklist.repository;

import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Date;

@Repository
public interface TasksRepository extends JpaRepository<Tasks, Integer> {

    @Query("SELECT t FROM Tasks t WHERE t.program.id = :programId")
    Page<Tasks> listTasksOfProgram(int programId, Pageable pageable);

    @Query("SELECT t FROM Tasks t JOIN Program p On t.program.id = p.id " +
            "WHERE p.id = :programId " +
            "AND (:status IS NULL OR t.status = :status)" +
            "AND (:taskName IS NULL OR t.taskName like %:taskName%)" +
            "AND (:endTime IS NULL OR t.endTime = :endTime)")
    Page<Tasks> findByProgramIdAndFilter(int programId, String status, String taskName, LocalDate endTime , Pageable pageable);
}
