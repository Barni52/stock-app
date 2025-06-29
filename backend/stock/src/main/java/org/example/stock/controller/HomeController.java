package org.example.stock.controller;

import org.example.stock.model.Stock;
import org.example.stock.repository.StockRepository;
import org.example.stock.repository.StockUserRepository;
import org.example.stock.service.StockParsingService;
import org.example.stock.service.TwelveDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class HomeController {

    private final StockUserRepository userRepository;
    private final TwelveDataService twelveDataService;
    private final StockRepository stockRepository;
    private final StockParsingService stockParsingService;

    public HomeController(StockUserRepository userRepository,
                          TwelveDataService twelveDataService,
                          StockRepository stockRepository,
                          StockParsingService stockParsingService) {
        this.userRepository = userRepository;
        this.twelveDataService = twelveDataService;
        this.stockRepository = stockRepository;
        this.stockParsingService = stockParsingService;
    }

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @PreAuthorize("#username == authentication.name")
    @GetMapping("/balance/{username}")
    public ResponseEntity<Double> getBalance(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String username){
        return userRepository.findByUsername(username)
                .map(u -> ResponseEntity.ok(u.getBalance()))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/test")
    public ResponseEntity<List<String>> getStickers() {
        String data = twelveDataService.getIntradayStockData("AAPL");

        Stock stock = stockParsingService.parse(data);

        stockRepository.save(stock);

        return ResponseEntity.ok(List.of(data));
    }
}
