package com.ya3k.checklist.service.serviceinterface;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.dto.response.programresponse.ProgramResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;

public interface ProgramService {
    ProgramDto createProgram(ProgramDto programDto, String userName);

    Page<ProgramResponse> findByUserAndFilters(String username, String status, LocalDate endTime, String programName, Pageable pageable);

    ProgramDto deleteById(int id);

    ProgramDto findByProgramId(int id);

    void autoUpdateStatusByTaskStatus(int taskId);

}
