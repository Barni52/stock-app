package org.example.stock.service;

import org.example.stock.model.Stock;
import org.example.stock.repository.StockRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ApiHandlerService {

    //private final String[] stockTickers = {"AAPL"};
    private final String[] stockTickers = {"AAPL","TSLA","PFE","NVDA","CAT","OTLY","SQ","NEE"};

    private final TwelveDataService twelveDataService;
    private final StockParsingService stockParsingService;
    private final StockRepository stockRepository;

    public ApiHandlerService(TwelveDataService twelveDataService, StockParsingService stockParsingService, StockRepository stockRepository) {
        this.twelveDataService = twelveDataService;
        this.stockParsingService = stockParsingService;
        this.stockRepository = stockRepository;
    }

    public void loadCurrentPrices(){
        for(String stockTicker : stockTickers){
            String jsonS = twelveDataService.getCurrentStockPrice(stockTicker);

            Double price = stockParsingService.parsePrice(jsonS);

            Optional<Stock> stockOptional = stockRepository.findById(stockTicker);

            if(stockOptional.isPresent()){
                Stock stock = stockOptional.get();

                stock.setCurrentPrice(price);

                stockRepository.save(stock);
            } else{
                Stock stock = new Stock();
                stock.setTicker(stockTicker);
                stock.setCurrentPrice(price);

                stockRepository.save(stock);
            }
        }
    }

    public void loadHistoricalDayPrice(){
        for(String stockTicker : stockTickers){
            String jsonS = twelveDataService.getIntradayStockData(stockTicker);

            Stock stock = stockParsingService.parse(jsonS);

            if(stock.getCurrentPrice() != null){
                stockRepository.save(stock);
            }
        }
    }
}
