package org.example.stock.service;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TwelveDataService {

    private final RestTemplate restTemplate;

    public TwelveDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getIntradayStockData(String symbol) {

        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("API_KEY");

        String url = "https://api.twelvedata.com/time_series?"
                +"apikey=" + apiKey
                + "&symbol=" + symbol
                + "&interval=30min"
                + "&outputsize=16";

        return restTemplate.getForObject(url, String.class);
    }

    public String getCurrentStockPrice(String symbol) {

        Dotenv dotenv = Dotenv.load();

        String apiKey = dotenv.get("API_KEY");

        String url = "https://api.twelvedata.com/time_series?"
                +"apikey=" + apiKey
                + "&symbol=" + symbol
                + "&interval=1min"
                + "&outputsize=1";

        return restTemplate.getForObject(url, String.class);
    }
}