package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.response.programresponse.ProgramResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface ProgramService {

    Page<ProgramResponse> findByUserAndFilters(String username, String status, LocalDate endTime, String programName, Pageable pageable);

    ProgramDto deleteById(int id);

    ProgramDto findByProgramId(int id);
}
