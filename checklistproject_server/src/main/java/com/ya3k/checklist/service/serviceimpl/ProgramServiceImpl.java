package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.mapper.ProgramMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProgramServiceImpl implements ProgramService {
    private ProgramRepository programRepository;
     private UserRepository userRepo;
    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository, UserRepository userRepo) {
        this.programRepository = programRepository;
        this.userRepo = userRepo;
    }



    @Override
    public List<ProgramDto> findProgramName(String name, Pageable pageable) {
        Page<Program> programs = programRepository.findByNameContaining(name, pageable);
        if(programs != null && !programs.isEmpty()){
            return programs.getContent().stream()
                    .map(ProgramMapper::toDto)
                    .collect(Collectors.toList());
        }
       return null;
    }

    @Override
    public List<ProgramDto> seachProgramName(String userName, String pName, Pageable pageable) {
        Page<Program> programs = programRepository.findByNameAndUserName(userName, pName, pageable);
        if(programs != null && !programs.isEmpty()){
            return programs.getContent().stream()
                    .map(ProgramMapper::toDto)
                    .collect(Collectors.toList());
        }
        return null;
    }


}
