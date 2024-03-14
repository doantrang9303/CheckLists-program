package com.ya3k.checklist.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.ya3k.checklist.repository.ProgramRepository;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
@Getter
@Setter
public class ProgramListResponse {
    @JsonProperty("program_list")
    public List<ProgramResponse> programResponseList;

    @JsonProperty( "total")
    public int total;
    @JsonProperty("total_page")
    public int totalPage;

}
