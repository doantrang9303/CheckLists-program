package com.ya3k.checklist.event.eventhandle;

import com.ya3k.checklist.entity.Program;
import org.springframework.context.ApplicationEvent;

public class ProgramEventHandle extends ApplicationEvent {
    private Program program;

    public ProgramEventHandle(Object source, Program program) {
        super(source);
        this.program = program;
    }


    public Program getProgram() {
        return program;
    }
}
