package com.example.movieAPI;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
@Slf4j
public class Scheduler {

    @Scheduled(fixedDelayString = "PT01M",initialDelay = 5000)
    public void scheduler() throws InterruptedException {
        LocalDateTime current = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        System.out.println("Hello Ji :"+current.format(formatter));
        Thread.sleep(500);
    }
}
