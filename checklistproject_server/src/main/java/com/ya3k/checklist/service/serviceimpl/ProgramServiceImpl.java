package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.Enum.StatusEnum;
import com.ya3k.checklist.dto.ProgramDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.entity.Users;
import com.ya3k.checklist.event.eventhandle.ProgramEventHandle;
import com.ya3k.checklist.mapper.ProgramMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.repository.UserRepository;
import com.ya3k.checklist.dto.response.programresponse.ProgramResponse;
import com.ya3k.checklist.service.serviceinterface.ProgramService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class ProgramServiceImpl implements ProgramService {



    private final ProgramRepository programRepository;
private final TasksRepository tasksRepository;
    private final UserRepository urepo;
    private final ApplicationEventPublisher eventPublisher;


    @Autowired
    public ProgramServiceImpl(ProgramRepository programRepository, TasksRepository tasksRepository, UserRepository urepo, ApplicationEventPublisher eventPublisher) {
        this.programRepository = programRepository;
        this.tasksRepository = tasksRepository;
        this.urepo = urepo;
        this.eventPublisher = eventPublisher;
    }


    @Override
    public ProgramDto createProgram(ProgramDto programDto,String userName) {
        Program program = ProgramMapper.mapDtoToProgram(programDto);
        Users user = urepo.findByUser(userName);
        program.setUser(user);
        if (programDto.getName() != null) {
            String trimmedName = programDto.getName().trim();
                program.setName(trimmedName);
        }

        if (program.getStatus() == null || program.getStatus().isEmpty()){
            program.setStatus(StatusEnum.IN_PROGRESS.getStatus());
        }

        //set create time
        program.setCreate_time(LocalDateTime.now());


        if (programDto.getEndTime() != null) {
            program.setEndTime(programDto.getEndTime());
        }

        Program savedProgram = programRepository.save(program);
        return ProgramMapper.mapToDto(savedProgram);
    }

    @Override
    public Page<ProgramResponse> findByUserAndFilters(String username, String status, LocalDate endTime, String programName, Pageable pageable) {
        Page<Program> programs = programRepository.findByUserAndFilters(username, status, endTime, programName, pageable);

//        if (programs.isEmpty()) {
//            throw new IllegalArgumentException("There are no programs with the given filters.");
//        }
        return programs.map(ProgramResponse::fromProgram);
    }

    @Override
    public ProgramDto deleteById(int id) {

        Program program = programRepository.deleteById(id);
        if (program != null) {
            return ProgramMapper.mapToDto(program);
        }
        return null;
    }

    @Override
    public ProgramDto findByProgramId(int id) {
        Program program = programRepository.findByProgramId(id);
        if (program != null) {
            return ProgramMapper.mapToDto(program);
        }
        return null;
    }

    @Override
    public void autoUpdateStatusByTaskStatus(int taskId) {
        Tasks tasks = tasksRepository.findByTasksId(taskId);
        //handler auto update status for program
        Program program = tasks.getProgram();
        List<Tasks> allTasks = program.getListTask();
        boolean allTasksCompleted = allTasks.stream().allMatch(t -> StatusEnum.COMPLETED.getStatus().equals(t.getStatus()));

        // Update program status if all tasks are completed
        if (allTasksCompleted) {
            program.setStatus(StatusEnum.COMPLETED.getStatus());
        } else {
            program.setStatus(StatusEnum.IN_PROGRESS.getStatus());
        }
        programRepository.save(program);
        // Publish program status change event
        eventPublisher.publishEvent(new ProgramEventHandle(this, program));
    }


}
