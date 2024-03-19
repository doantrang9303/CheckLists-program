    package com.ya3k.checklist.repository;

    import com.ya3k.checklist.entity.Program;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.time.LocalDate;
    import java.util.List;

    public interface ProgramRepository extends JpaRepository<Program, Integer> {

        @Query("SELECT p FROM Program p JOIN p.user u WHERE u.user_name = :username " +
                "AND (:status IS NULL OR p.status = :status) " +
                "AND (:endTime IS NULL OR p.endTime = :endTime)" +
                "AND (:programName IS NULL OR p.name like %:programName%)" +
                "ORDER BY p.create_time DESC")
        Page<Program> findByUserAndFilters(String username,
                                           String status,
                                           LocalDate endTime,
                                           String programName, 
                                           Pageable pageable);

        Program deleteById(int id);


        @Query("SELECT p FROM Program p WHERE p.id = :id")
        Program findByProgramId(int id);
        
    }
