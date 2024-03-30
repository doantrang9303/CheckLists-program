package com.ya3k.checklist.Enum;

import lombok.Getter;

@Getter
public enum StatusEnum {

    PENDING(1, "PENDING"),
    IN_PROGRESS(2, "IN_PROGRESS"),
    COMPLETED(3, "COMPLETED"),
    MISS_DEADLINE(4, "MISS_DEADLINE");


    private int id;
    private String status;

    StatusEnum(int id, String status) {
        this.id = id;
        this.status = status;
    }


}
