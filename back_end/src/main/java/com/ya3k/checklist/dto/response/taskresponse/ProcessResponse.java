package com.ya3k.checklist.dto.response.taskresponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessResponse {
    public String mess;
    public  int totalCount;
    public int Count;

    public ProcessResponse(String mess, int totalCount, int Count) {
        this.mess = mess;
        this.totalCount = totalCount;
        this.Count = Count;
    }
}
