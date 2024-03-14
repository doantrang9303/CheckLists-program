package com.ya3k.checklist.mapper;

import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;

public class ProgramMapper {

    public static ProgramDto toDto(Program program) {
        if (program == null) {
            return null;
        }

        ProgramDto dto = new ProgramDto();
        dto.setId(program.getId());
        dto.setName(program.getName());
        dto.setUser(program.getUser());
        dto.setStatus(program.getStatus());
        dto.setCreate_time(program.getCreate_time());
        dto.setEnd_time(program.getEnd_time());

        return dto;
    }

    public static Program toEntity(ProgramDto dto) {
        if (dto == null) {
            return null;
        }

        Program entity = new Program();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setUser(dto.getUser());
        entity.setStatus(dto.getStatus());
        entity.setCreate_time(dto.getCreate_time());
        entity.setEnd_time(dto.getEnd_time());

        return entity;
    }
}
