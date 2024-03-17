package com.ya3k.checklist.response.taskresponse;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.List;

@AllArgsConstructor
@Data
@NoArgsConstructor
@Builder
public class TasksListResponse {
    @JsonProperty("task_list")
    public List<TasksResponse> tasksResponseList;

    @JsonProperty("total")
    public int total;
    @JsonProperty("total_page")
    public int totalPage;

}
