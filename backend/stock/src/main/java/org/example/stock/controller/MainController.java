package org.example.stock.controller;

import org.example.stock.model.Stock;
import org.example.stock.model.StockUser;
import org.example.stock.repository.StockRepository;
import org.example.stock.repository.StockUserRepository;
import org.example.stock.service.StockParsingService;
import org.example.stock.service.TwelveDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final StockUserRepository userRepository;
    private final TwelveDataService twelveDataService;
    private final StockRepository stockRepository;
    private final StockParsingService stockParsingService;
    private final StockUserRepository stockUserRepository;

    public MainController(StockUserRepository userRepository,
                          TwelveDataService twelveDataService,
                          StockRepository stockRepository,
                          StockParsingService stockParsingService, StockUserRepository stockUserRepository) {
        this.userRepository = userRepository;
        this.twelveDataService = twelveDataService;
        this.stockRepository = stockRepository;
        this.stockParsingService = stockParsingService;
        this.stockUserRepository = stockUserRepository;
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

    @PreAuthorize("#username == authentication.name")
    @PutMapping("/balance/upload/{username}")
    public ResponseEntity<Void> uploadBalance(@RequestParam Double value, @PathVariable String username){
        Optional<StockUser> stockUserOptional = stockUserRepository.findByUsername(username);
        if(stockUserOptional.isPresent()){
            StockUser stockUser = stockUserOptional.get();
            stockUser.setBalance(stockUser.getBalance() + value);
            stockUserRepository.save(stockUser);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.status(400).build();
    }

    @PreAuthorize("#username == authentication.name")
    @PutMapping("/balance/withdraw/{username}")
    public ResponseEntity<Void> withdrawBalance(@RequestParam Double value, @PathVariable String username){
        Optional<StockUser> stockUserOptional = stockUserRepository.findByUsername(username);

        if(stockUserOptional.isPresent()){
            StockUser stockUser = stockUserOptional.get();
            if(value <= stockUser.getBalance()){
                stockUser.setBalance(stockUser.getBalance() - value);
                stockUserRepository.save(stockUser);
                return ResponseEntity.ok().build();
            } else{
                return ResponseEntity.status(401).build();
            }
        }
        return ResponseEntity.status(400).build();
    }
}
