package com.ya3k.checklist.dto.response.programresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProgramResponse {

    private int id;
    private String name;
    @JsonProperty("user_name")
    private String userName;
    private String status;
    private LocalDateTime createTime;
    private LocalDate endTime;

    public static ProgramResponse fromProgram(Program program) {
        return ProgramResponse.builder()
                .id(program.getId())
                .name(program.getName())
                .userName(program.getUser().getUser_name())
                .status(program.getStatus())
                .createTime(program.getCreateTime())
                .endTime(program.getEndTime())
                .build();
    }
}
