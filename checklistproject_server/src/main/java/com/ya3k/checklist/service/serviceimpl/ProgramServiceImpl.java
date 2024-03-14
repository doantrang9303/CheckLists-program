package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.mapper.ProgramMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.response.ProgramResponse;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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
    public List<ProgramDto> findProgramName(String name, Pageable pageable) {
        Page<Program> programs = programRepository.findByNameContaining(name, pageable);
        return programs.getContent().stream()
                .map(ProgramMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<ProgramResponse> seachProgramName(String userName, String pName, Pageable pageable) {
        Page<Program> programs = programRepository.findByNameAndUserName(userName, pName, pageable);
        return programs.map(ProgramResponse::fromProgram);
    }

    @Override
    public Page<ProgramResponse> findByUserName(String userName, Pageable pageable) {
       Page<Program> programs = programRepository.findByUserName(userName, pageable);
       return programs.map(ProgramResponse::fromProgram);
    }

    @Override
    public Page<ProgramResponse> findByUserAndFilters(String username, String status, String programName, Pageable pageable) {
        Page<Program> programs = programRepository.findByUserAndFilters(username, status, programName, pageable);
        return programs.map(ProgramResponse::fromProgram);
    }

}
