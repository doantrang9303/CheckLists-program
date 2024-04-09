package com.ya3k.checklist.mapper;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;


public class ProgramMapper {

    public static ProgramDto mapToDto(Program program) {
        return new ProgramDto(
                program.getId(),
                program.getName(),
                program.getUser().getUser_name(),
                program.getStatus(),
                program.getCreateTime(),
                program.getEndTime()
        );
    }

    public static Program mapDtoToProgram(ProgramDto programDto) {
        Program program = new Program();
        program.setId(programDto.getId());
        program.setName(programDto.getName());
        program.setStatus(programDto.getStatus());
        program.setCreateTime(programDto.getCreateTime());
        program.setEndTime(programDto.getEndTime());
        return program;
    }
}
