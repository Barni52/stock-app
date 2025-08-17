package org.example.stock.controller;

import org.example.stock.dto.AuthRequest;
import org.example.stock.dto.AuthResponse; // Make sure this is correctly imported
import org.example.stock.repository.StockUserRepository;
import org.example.stock.config.jwt.JwtUtil;
import org.example.stock.model.StockUser;
import org.example.stock.service.StockUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authManager;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private StockUserDetailsService userDetailsService;
    @Autowired
    private StockUserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            authManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            System.out.println("Authentication failed for user: " + request.getUsername() + " - " + e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        final UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        final String jwt = jwtUtil.generateToken(userDetails.getUsername());

        // --- ADD THESE DEBUG PRINTS ---
        System.out.println("Backend: Generated JWT for " + userDetails.getUsername() + ": " + jwt);
        AuthResponse authResponse = new AuthResponse(jwt);
        System.out.println("Backend: AuthResponse object before sending: " + authResponse.getToken());
        // --- END DEBUG PRINTS ---

        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody AuthRequest request) {
        if(userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists");
        }
        StockUser newUser = new StockUser();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setBalance(0);

        userRepository.save(newUser);

        return ResponseEntity.ok("User registered successfully");
    }
}