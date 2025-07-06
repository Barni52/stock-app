package org.example.stock.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.stock.model.Stock;
import org.example.stock.model.TimePrice;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class StockParsingService {

    private static final DateTimeFormatter DTF =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final ObjectMapper mapper;

    public StockParsingService(ObjectMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Parse only the symbol (ticker) and each "values" entry's datetime & open.
     * All other fields in the JSON are ignored.
     */
    public Stock parse(String rawJson)  {
        JsonNode root = null;
        try {
            root = mapper.readTree(rawJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // grab ticker
        String ticker = root
                .path("meta")
                .path("symbol")
                .asText();

        // build list of TimePrice
        Stock stock = new Stock();
        List<TimePrice> list = new ArrayList<>();
        for (JsonNode val : root.path("values")) {
            String dt = val.path("datetime").asText();
            String open = val.path("open").asText();

            LocalDateTime date = LocalDateTime.parse(dt, DTF);
            double price = Double.parseDouble(open);

            TimePrice timePrice = new TimePrice();
            timePrice.setPrice(price);
            timePrice.setTime(date);
            timePrice.setStock(stock);

            list.add(timePrice);
        }

        stock.setTicker(ticker);
        stock.setTimePrice(list);
        return stock;
    }

    public Double parsePrice(String json){
        JSONObject obj = new JSONObject(json);
        JSONArray values = obj.getJSONArray("values");
        JSONObject firstEntry = values.getJSONObject(0);
        double close = Double.parseDouble(firstEntry.getString("close"));

        return close;
    }
}
