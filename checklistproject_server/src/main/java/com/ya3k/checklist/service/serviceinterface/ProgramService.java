package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramService {
    List<ProgramDto> findProgramName(String name, Pageable pageable);
    List<ProgramDto> findProgramStatus(String status, Pageable pageable);
    List<ProgramDto> findProgramUser(int id, Pageable pageable);

}
