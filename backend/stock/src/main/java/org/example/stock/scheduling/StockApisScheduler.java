package org.example.stock.scheduling;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockApisScheduler {
    // Every 30 minutes (in milliseconds)
    @Scheduled(fixedRate = 30 * 60 * 1000)
    //@Scheduled(fixedRate = 1 * 1 * 1000)
    public void doSomething() {
        System.out.println("Running task at: " + java.time.LocalDateTime.now());
    }
}
