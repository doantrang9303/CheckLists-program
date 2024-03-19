package com.ya3k.checklist.service.serviceimpl;

import com.ya3k.checklist.dto.TasksDto;
import com.ya3k.checklist.entity.Program;
import com.ya3k.checklist.entity.Tasks;
import com.ya3k.checklist.mapper.TasksMapper;
import com.ya3k.checklist.repository.ProgramRepository;
import com.ya3k.checklist.repository.TasksRepository;
import com.ya3k.checklist.response.taskresponse.TasksResponse;
import com.ya3k.checklist.service.serviceinterface.TasksService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
public class TaskServiceImpl implements TasksService {
    private final TasksRepository tasksRepository;
    private final ProgramRepository programRepository;

    @Autowired
    public TaskServiceImpl(TasksRepository tasksRepository, ProgramRepository programRepository) {
        this.programRepository = programRepository;
        this.tasksRepository = tasksRepository;
    }

    @Override
    public Page<TasksResponse> listTasksOfProgram(int programId, Pageable pageable) {
        Optional<Program> programs = programRepository.findById(programId);
        if (programs.isEmpty()) {
            throw new IllegalArgumentException("Program not existed");
        }

        Page<Tasks> tasks = tasksRepository.listTasksOfProgram(programId, pageable);

        return tasks.map(TasksResponse::fromTasks);
    }

    @Override
    public Page<TasksResponse> findByProgramIdAndFilter(int programId, String status, String taskName, LocalDate endTime, Pageable pageable) {
        Optional<Program> programs = programRepository.findById(programId);

        if (programs.isEmpty()) {
            throw new IllegalArgumentException("Program not existed");
        }

        Page<Tasks> tasks = tasksRepository.findByProgramIdAndFilter(programId, status, taskName, endTime, pageable);




        return tasks.map(TasksResponse::fromTasks);
    }

    @Override
    public TasksDto deleteById(int id) {
        Tasks tasks = tasksRepository.deleteById(id);
        if(tasks != null) {
            return TasksMapper.tasksToDto(tasks);
        }
        return null;
    }

    @Override
    public TasksDto findByTaskId(int id) {
        Tasks tasks = tasksRepository.findByTasksId(id);
        if(tasks != null) {
            return TasksMapper.tasksToDto(tasks);
        }
        return null;
    }

    @Override
    public void inportTask(MultipartFile file, int programId) {
        try {

            try (InputStream inputStream = file.getInputStream()) {
                Workbook workbook = new XSSFWorkbook(inputStream);
                Sheet datatypeSheet = workbook.getSheetAt(0);
                Iterator<Row> iterator = datatypeSheet.iterator();

                // Skip header row if needed
                if (iterator.hasNext()) {
                    iterator.next(); // Skip header row
                }

                while (iterator.hasNext()) {
                    Row currentRow = iterator.next();
                    Tasks task = new Tasks();
                    if (currentRow.getCell(0) ==null){
                        continue;
                    }

                    Cell taskNameCell = currentRow.getCell(1);
                    task.setTaskName(taskNameCell.getStringCellValue());

                    Program program = new Program();
                    program.setId(programId);
                    task.setProgram(program);

                    Cell statusCell = currentRow.getCell(2);
                    task.setStatus(statusCell.getStringCellValue());

                    Cell createTimeCell = currentRow.getCell(3);
                    LocalDateTime createTime = LocalDateTime.parse(createTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                    task.setCreateTime(createTime);

                    Cell endTimeCell = currentRow.getCell(4);
                    LocalDate endTime = LocalDate.parse(endTimeCell.getStringCellValue(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                    task.setEndTime(endTime);

                    tasksRepository.save(task);
                }

                workbook.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


}
