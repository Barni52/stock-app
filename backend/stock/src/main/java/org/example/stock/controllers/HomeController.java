package org.example.stock.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Map;

@Controller
public class HomeController {

    @RequestMapping("/")
    public String index(){
        return "index.html";
    }

    @CrossOrigin(origins = "http://localhost:5173")
    @GetMapping("/num")
    public ResponseEntity<Map<String, Integer>> getNum(){
        return ResponseEntity.ok(Map.of("number", 42));
    }
}
