package org.example.stock.controller;

import org.example.stock.repository.StockUserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {

    private final StockUserRepository userRepository;

    public HomeController(StockUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/balance/{username}")
    public ResponseEntity<Double> getBalance(
            @AuthenticationPrincipal UserDetails user,
            @PathVariable String username){
        return userRepository.findByUsername(username)
                .map(u -> ResponseEntity.ok(u.getBalance()))
                .orElse(ResponseEntity.notFound().build());
    }
}
