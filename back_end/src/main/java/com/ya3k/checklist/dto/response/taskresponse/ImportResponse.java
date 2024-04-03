package com.ya3k.checklist.dto.response.taskresponse;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImportResponse {
    private String mess;
    private int totalCount;
    private int savedCount;

    public ImportResponse(String mess, int totalCount, int savedCount) {
        this.mess = mess;
        this.totalCount = totalCount;
        this.savedCount = savedCount;
    }

}
