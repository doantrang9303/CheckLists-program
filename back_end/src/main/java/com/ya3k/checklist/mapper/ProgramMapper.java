package com.ya3k.checklist.mapper;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;

import java.time.ZoneId;
import java.util.Date;

public class ProgramMapper {



    public static ProgramDto mapToDto(Program program) {
        return new ProgramDto(
                program.getId(),
                program.getName(),
                program.getUser().getUser_name(),
                program.getStatus(),
                program.getCreate_time(),
                program.getEndTime()
        );
    }
    public static Program mapDtoToProgram(ProgramDto programDto) {
        Program program = new Program();
        program.setId(programDto.getId());
        program.setName(programDto.getName());
        // Assuming there's a separate service to fetch User by ID and set it to Program
        // program.setUser(userService.getUserById(programDto.getUserId()));
        program.setStatus(programDto.getStatus());
        program.setCreate_time(programDto.getCreateTime());
        program.setEndTime(programDto.getEndTime());
        return program;
    }
}
