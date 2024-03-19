package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.mapper.ProgramMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.response.programresponse.ProgramResponse;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramServiceImpl implements ProgramService {

    private final ProgramRepository programRepository;

    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    @Override
    public Page<ProgramResponse> findByUserAndFilters(String username, String status, LocalDate endTime, String programName, Pageable pageable) {

        Page<Program> programs = programRepository.findByUserAndFilters(username, status, endTime, programName, pageable);
        if (programs.isEmpty()) {
            throw new IllegalArgumentException("There are no programs with the given filters.");
        }
        return programs.map(ProgramResponse::fromProgram);
    }

        Page<Program> programs = programRepository.findByUserAndFilters(username, status, endTime, programName, pageable);
//        if (programs.isEmpty()) {
//            throw new IllegalArgumentException("There are no programs with the given filters.");
//        }
        return programs.map(ProgramResponse::fromProgram);
    }

    @Override
    public ProgramDto deleteById(int id) {

        Program program = programRepository.deleteById(id);
        if (program != null) {
            return ProgramMapper.mapToDto(program);
        }
        return null;
    }

    @Override
    public ProgramDto findByProgramId(int id) {
        Program program = programRepository.findByProgramId(id);
        if (program != null) {
            return ProgramMapper.mapToDto(program);
        }
        return null;
    }


}
