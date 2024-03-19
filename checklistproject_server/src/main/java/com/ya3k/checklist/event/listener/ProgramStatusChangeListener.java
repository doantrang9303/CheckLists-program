package com.ya3k.checklist.event.listener;

import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.event.eventhandle.ProgramEventHandle;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

@Component
public class ProgramStatusChangeListener implements ApplicationListener<ProgramEventHandle> {
    @Override
    public void onApplicationEvent(ProgramEventHandle event) {
        Program program = event.getProgram();

        //send mess
        System.out.println("Program " +program.getName() + " status change to: " + program.getStatus());
    }


}
