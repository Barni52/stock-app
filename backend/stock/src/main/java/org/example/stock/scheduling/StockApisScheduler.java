package org.example.stock.scheduling;

import org.example.stock.service.ApiHandlerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockApisScheduler {

    private final ApiHandlerService apiHandlerService;

    public StockApisScheduler(ApiHandlerService apiHandlerService) {
        this.apiHandlerService = apiHandlerService;
    }

    @Scheduled(fixedRate = 30 * 60 * 1000)
    public void fetchHistoricalPriceForDay() {
        //apiHandlerService.loadHistoricalDayPrice();
    }

    @Scheduled(fixedRate = 60 * 1000, initialDelay = 60 * 1000)
    public void fetchCurrentPrice() {
        //apiHandlerService.loadCurrentPrices();
    }
}
