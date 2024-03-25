package com.ya3k.checklist.controller;

import com.ya3k.checklist.service.serviceimpl.EmailServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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

    @Scheduled(cron = "${cron expression}")
    public void sendEmails() {
        // Query
        List<String> emails = jdbcTemplate.queryForList("SELECT DISTINCT ui.email FROM user_info ui " +
                "INNER JOIN users u ON ui.user_id = u.user_id " +
                "INNER JOIN programs p ON p.user_id = u.user_id " +
                "WHERE p.status <> 'COMPLETED'", String.class);
        List<String> user = jdbcTemplate.queryForList("SELECT DISTINCT users.user_name FROM users " +
                "JOIN programs ON users.user_id = programs.user_id" +
                " WHERE programs.status <> 'COMPLETED'", String.class);

        if (emails.isEmpty()) {
            log.info("khong tim duoc user chua hoan thanh program");
        } else {
            String subject = " REMINDER TO COMPLETE PROGRAM ";
            String body = "Hello <br/>You have program not done. Please " +
                    "<a href=https://github.com/doantrang9303/CheckLists-program> click the link </a>" +    //thay link nay bang link trang chu project
                    " to check";
            String[] recipients = emails.toArray(new String[0]);
            emailService.sendEmail(recipients, subject, body);
            log.info("{}: email send "+user+" success", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));

        }
    }
}