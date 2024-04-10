package com.ya3k.checklist.controller;

import com.ya3k.checklist.service.EmailServiceImpl;
import com.ya3k.checklist.design.EmailDesign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@EnableScheduling
@Slf4j
public class EmailJob {
    @Autowired
    private EmailServiceImpl emailService;
    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final EmailDesign design;
    @Autowired
    public EmailJob(EmailDesign design) {
        this.design = design;
    }
    @Scheduled(cron = "${cron expression}")
    public void sendEmails() throws IOException {
        List<String> emails = jdbcTemplate.queryForList("SELECT DISTINCT u.email FROM users u " +
                "JOIN programs p ON u.user_id = p.user_id " +
                "WHERE p.status <> 'COMPLETED'", String.class);
        List<String> user = jdbcTemplate.queryForList("SELECT DISTINCT users.user_name FROM users " +
                "JOIN programs ON users.user_id = programs.user_id" +
                " WHERE programs.status <> 'COMPLETED'", String.class);
        List<String> dlineProgram = jdbcTemplate.queryForList("SELECT COUNT(p.id) AS program_count FROM users U" +
                " JOIN programs p ON u.user_id = p.user_id" +
                " WHERE p.status <> 'COMPLETED' GROUP BY u.email;", String.class);
        List<String> dlineTask = jdbcTemplate.queryForList("SELECT COUNT(t.id) AS task_count FROM users u" +
                " JOIN programs p ON u.user_id = p.user_id" +
                " JOIN tasks t ON p.id=t.program_id"+
                " WHERE t.end_time =  DATEADD(DAY, 1, CAST(GETDATE() AS DATE) ) GROUP BY u.email;", String.class);

        if (emails.isEmpty()) {
            log.info("khong tim duoc user chua hoan thanh program");
        } else {
            String subject = "REMINDER TO COMPLETE PROGRAM";
            for (int i = 0; i < emails.size(); i++) {
                String email = emails.get(i);
                String users = user.get(i);
                String dlTimeProgram=dlineProgram.get(i);
                String dlTimeTask=dlineTask.isEmpty() ? "0" : dlineTask.get(i);
                
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
                LocalDateTime hientai = LocalDateTime.now();
                LocalDateTime ngaySau = hientai.plusDays(1);
                String formattedString = ngaySau.format(formatter);

                String body=design.buildEmailBody(users,dlTimeProgram,dlTimeTask,formattedString);
                emailService.sendEmail(new String[]{email}, subject, body);
            }
            log.info("{}: email send "+user+" success", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        }
    }
}

