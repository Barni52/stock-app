package org.example.stock.controller;

import org.example.stock.exception.*;
import org.example.stock.model.OwnedStock;
import org.example.stock.model.StockOrder;
import org.example.stock.model.Stock;
import org.example.stock.model.StockUser;
import org.example.stock.repository.OrderRepository;
import org.example.stock.repository.OwnedStockRepository;
import org.example.stock.repository.StockRepository;
import org.example.stock.repository.StockUserRepository;
import org.example.stock.service.StockParsingService;
import org.example.stock.service.TradingService;
import org.example.stock.service.TwelveDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
public class MainController {

    private final StockUserRepository userRepository;
    private final TwelveDataService twelveDataService;
    private final StockParsingService stockParsingService;
    private final StockUserRepository stockUserRepository;
    private final TradingService tradingService;
    private final StockRepository stockRepository;
    private final OwnedStockRepository ownedStockRepository;

    public MainController(StockUserRepository userRepository,
                          TwelveDataService twelveDataService,
                          StockParsingService stockParsingService,
                          StockUserRepository stockUserRepository,
                          TradingService tradingService,
                          StockRepository stockRepository,
                          OwnedStockRepository ownedStockRepository) {
        this.userRepository = userRepository;
        this.twelveDataService = twelveDataService;
        this.stockParsingService = stockParsingService;
        this.stockUserRepository = stockUserRepository;
        this.tradingService = tradingService;
        this.stockRepository = stockRepository;
        this.ownedStockRepository = ownedStockRepository;
    }

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/stock")
    public ResponseEntity<List<Stock>> getAllStocks(){
        return ResponseEntity.ok(stockRepository.findAll());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("#username == authentication.name")
    @GetMapping("/stock/owned/{username}")
    public ResponseEntity<List<Stock>> getOwnedStocks(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String username){
        Optional<StockUser> stockUserOptional = userRepository.findByUsername(username);

        StockUser stockUser;

        if(stockUserOptional.isPresent()){
            stockUser = stockUserOptional.get();
        } else{
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(ownedStockRepository.findOwnedStocksByStockUser(stockUser)
                .stream().map(OwnedStock::getStock).toList());
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("#username == authentication.name")
    @GetMapping("/balance/{username}")
    public ResponseEntity<Double> getBalance(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String username){
        return userRepository.findByUsername(username)
                .map(u -> ResponseEntity.ok(u.getBalance()))
                .orElse(ResponseEntity.notFound().build());
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/test")
    public ResponseEntity<List<String>> getStickers() {

        String data = twelveDataService.getCurrentStockPrice("NEE");

        System.out.println(data);

        Double price = stockParsingService.parsePrice(data);

        System.out.println("The price is : " + price);

        return ResponseEntity.ok(List.of("data"));
    }

    @CrossOrigin(origins = "http://localhost:4200")

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

    @CrossOrigin(origins = "http://localhost:4200")

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

    @CrossOrigin(origins = "http://localhost:4200")

    @PreAuthorize("#username == authentication.name")
    @PostMapping("/order/buy/{username}")
    @Transactional
    public ResponseEntity<Void> makeBuyOrder(
            @PathVariable String username,
            @RequestParam String ticker,
            @RequestParam Double quantity,
            @RequestParam Double hitPrice
    ){
        try {
            StockOrder stockOrder = tradingService.makeBuyOrder(username, ticker, quantity, hitPrice);
            return ResponseEntity.ok().build();
        } catch (IncorrectInputException e) {
            ResponseEntity.badRequest().build();
        } catch (UserNotFoundException | StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientFundException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(500).build();
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("#username == authentication.name")
    @PostMapping("/order/sell/{username}")
    @Transactional
    public ResponseEntity<Void> makeSellOrder(
            @PathVariable String username,
            @RequestParam String ticker,
            @RequestParam Double quantity,
            @RequestParam Double hitPrice
    ){
        try {
            StockOrder stockOrder = tradingService.makeSellOrder(username, ticker, quantity, hitPrice);
            return ResponseEntity.ok().build();
        } catch (IncorrectInputException e) {
            ResponseEntity.badRequest().build();
        } catch (UserNotFoundException | StockNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InsufficientStockException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        return ResponseEntity.status(500).build();
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @PreAuthorize("#username == authentication.name")
    @DeleteMapping("/order/cancel/{username}")
    @Transactional
    public ResponseEntity<Void> cancelOrder(
            @PathVariable String username,
            @RequestParam String orderId
    ){
        try {
            tradingService.cancelOrder(orderId, username);
        } catch (UserNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok().build();
    }
}
