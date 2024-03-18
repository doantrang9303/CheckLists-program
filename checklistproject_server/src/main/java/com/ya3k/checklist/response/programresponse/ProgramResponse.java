package com.ya3k.checklist.response.programresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Program;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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
    private LocalDateTime create_time;
    private LocalDate end_time;

    public static ProgramResponse fromProgram(Program program) {
        ProgramResponse programResponse = ProgramResponse.builder()
                .id(program.getId())
                .name(program.getName())
                .userName(program.getUser().getUser_name())
                .status(program.getStatus())
                .create_time(program.getCreate_time())
                .end_time(program.getEndTime())
                .build();
        return programResponse;
    }


}
