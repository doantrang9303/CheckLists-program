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
                program.getUser() != null ? program.getUser().getUser_id() : 0,
                program.getStatus(),
                program.getCreate_time(),
                program.getEnd_time()
        );
    }

    public static Program mapDtoToProgram(ProgramDto programDto) {
        Program program = new Program();
        program.setId(programDto.getId());
        program.setName(programDto.getName());
        // Assuming there's a separate service to fetch User by ID and set it to Program
        // program.setUser(userService.findById(programDto.getUser_id()));
        program.setStatus(programDto.getStatus());
        program.setCreate_time(programDto.getCreate_time());
        program.setEnd_time(programDto.getEnd_time());
        return program;
    }
}
