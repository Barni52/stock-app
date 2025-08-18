package org.example.stock.scheduling;

import org.example.stock.service.ApiHandlerService;
import org.example.stock.service.TradeExecutionService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class StockApisScheduler {

    private final static int second = 1000;
    private final static int minute  = second * 60;

    private final ApiHandlerService apiHandlerService;
    private final TradeExecutionService tradeExecutionService;

    public StockApisScheduler(ApiHandlerService apiHandlerService, TradeExecutionService tradeExecutionService) {
        this.apiHandlerService = apiHandlerService;
        this.tradeExecutionService = tradeExecutionService;
    }

    //Every 30 minutes
    @Scheduled(fixedRate = 30 * minute, initialDelay = 20 * second)
    public void fetchHistoricalPriceForDay() {
        apiHandlerService.loadHistoricalDayPrice();
    }

    //Every 1 minute
    @Scheduled(fixedRate = 60 * second)
    public void fetchCurrentPrice() {
        apiHandlerService.loadCurrentPrices();
    }

    @Scheduled(fixedRate = second, initialDelay = 10 * second)
    public void executePossibleTrades() {
        tradeExecutionService.executeTrades();
    }
}
