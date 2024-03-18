    package com.ya3k.checklist.repository;

    import com.ya3k.checklist.entity.Program;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;
    import org.springframework.data.repository.query.Param;

    import java.time.LocalDate;

    public interface ProgramRepository extends JpaRepository<Program, Integer> {
        Page<Program> findByNameContaining(String name, Pageable pageable);

        @Query("SELECT p FROM Program p WHERE  p.user.user_name = :userName AND p.name like %:pName%")
        Page<Program> findByNameAndUserName(String userName, String pName, Pageable pageable);

        @Query("SELECT p FROM Program p WHERE  p.user.user_name = :userName order by p.create_time desc")
        Page<Program> findProgramByUserName(String userName, Pageable pageable);
        @Query("SELECT p FROM Program p JOIN p.user u WHERE u.user_name = :username " +
                "AND (:status IS NULL OR p.status = :status) " +
                "AND (:endTime IS NULL OR p.end_time = :endTime)"+
                "AND (:programName IS NULL OR p.name like %:programName%)")
        Page<Program> findByUserAndFilters(String username,
                                           String status,
                                           LocalDate endTime,
                                           String programName, Pageable pageable);

        Program deleteById(int id);

        @Query("SELECT p FROM Program p WHERE p.id = :id")
        Program findByProgramId(int id);
        
    }
