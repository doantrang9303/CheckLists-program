package com.ya3k.checklist.design;

import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Component
public class EmailDesign {
    public String getEmailTemplateContent() throws IOException {
        ClassPathResource resource = new ClassPathResource("FormatEmail/email-template.html");
        byte[] fileData = FileCopyUtils.copyToByteArray(resource.getInputStream());
        return new String(fileData, StandardCharsets.UTF_8);
    }
    public String buildEmailBody(String userName, String deadline) throws IOException {
        String emailTemplate = getEmailTemplateContent();
        emailTemplate = emailTemplate.replace("{{userName}}", userName);
        emailTemplate = emailTemplate.replace("{{deadline}}", deadline);
        return emailTemplate;
    }


}
