package org.example.stock.service;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class TwelveDataService {

    private final RestTemplate restTemplate;

    private String apiKey = "DFQWETX46FSABIMB";

    public TwelveDataService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getIntradayStockData(String symbol) {
        String url = "https://api.twelvedata.com/time_series?apikey=&outputsize=16"
                + "&symbol=" + symbol
                + "&interval=30min";

        return restTemplate.getForObject(url, String.class);
    }
}