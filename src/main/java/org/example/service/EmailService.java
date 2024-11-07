package org.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import javax.annotation.PostConstruct;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Configuration
class ExecutorConfig {

    @Bean
    public ScheduledExecutorService scheduledExecutorService() {
        return Executors.newScheduledThreadPool(5);
    }
}


@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ScheduledExecutorService scheduledExecutorService;

    @PostConstruct
    public void scheduleEmailSending() {
        scheduledExecutorService.scheduleWithFixedDelay(this::sendEmailsInLoop, 0, 10, TimeUnit.SECONDS);
    }

    @Scheduled(fixedDelay = 10000)
    public void sendEmailsInLoop() {
        for (int i = 0; i < 5; i++) {
            sendEmail("paymedia@example.com", "Scheduled Email", "This is a scheduled email.");
        }
    }


    @Async
    public void sendEmail(String to, String subject, String text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        mailSender.send(message);
        System.out.println("Email sent to " + to + " by " + Thread.currentThread().getName());
    }
}

