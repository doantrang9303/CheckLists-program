package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.response.ProgramResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProgramService {
    List<ProgramDto> findProgramName(String name, Pageable pageable);

    Page<ProgramResponse> seachProgramName(String userName, String pName, Pageable pageable);

    Page<ProgramResponse> findByUserName(String userName, Pageable pageable);

    Page<ProgramResponse> findByUserAndFilters(String username, String status, String programName, Pageable pageable);
}
