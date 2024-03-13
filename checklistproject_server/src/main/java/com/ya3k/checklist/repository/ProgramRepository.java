    package com.ya3k.checklist.repository;

    import com.ya3k.checklist.dto.ProgramDto;
    import com.ya3k.checklist.entity.Program;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.Pageable;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.data.jpa.repository.Query;

    import java.util.List;

    public interface ProgramRepository extends JpaRepository<Program, Integer> {
        Page<Program> findByNameContaining(String name, Pageable pageable);

        @Query("SELECT p FROM Program p WHERE  p.user.user_name = :userName AND p.name like %:pName%")
        Page<Program> findByNameAndUserName(String userName, String pName, Pageable pageable);

    }
