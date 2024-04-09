package com.ya3k.checklist.enumm;

import lombok.Getter;

@Getter
public enum TasksApiNoti {
    PROGRAMIDNOTVALID(1, "Program id is not valid"),
    PROGRAMNOTFOUND(2, "Program not found"),
    TASKIDNOTVALID(3, "Task id is not valid"),
    TASKNOTFOUND(4, "Task not found"),
    TASKNOTVALID(5, "Task is not valid"),
    UPDATESTATUSSUCCESS(6, "Update status of program by task status is successful"),
    CREATETASKSUCCESS(7, "Create task is successful"),
    REQUESTERROR(8, "An error occurred while processing the request: ");

    private int id;
    private String message;

    TasksApiNoti(int id, String message) {
        this.id = id;
        this.message = message;
    }
}
