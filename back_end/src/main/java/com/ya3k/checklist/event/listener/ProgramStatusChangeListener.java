package com.ya3k.checklist.event.listener;

import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.event.eventhandle.ProgramEventHandle;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProgramStatusChangeListener implements ApplicationListener<ProgramEventHandle> {
    @Override
    public void onApplicationEvent(ProgramEventHandle event) {
        Program program = event.getProgram();
        log.debug("Program " + program.getName() + " status: " + program.getStatus());
        
    }


}
