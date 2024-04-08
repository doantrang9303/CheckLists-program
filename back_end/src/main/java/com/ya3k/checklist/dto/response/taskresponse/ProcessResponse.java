package com.ya3k.checklist.dto.response.taskresponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessResponse {
    private String mess;
    private int total;
    private int saved;

    public ProcessResponse(String mess, int totalCount, int Count) {
        this.mess = mess;
        this.total = totalCount;
        this.saved = Count;
    }

}
