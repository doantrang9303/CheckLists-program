package com.ya3k.checklist.response;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Users;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.format.annotation.DateTimeFormat;

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
    private Date end_time;

    public static ProgramResponse fromProgram(Program program) {
        ProgramResponse programResponse = ProgramResponse.builder()
                .id(program.getId())
                .name(program.getName())
                .userName(program.getUser().getUser_name())
                .status(program.getStatus())
                .create_time(program.getCreate_time())
                .end_time(program.getEnd_time())
                .build();
        return programResponse;
    }


}
