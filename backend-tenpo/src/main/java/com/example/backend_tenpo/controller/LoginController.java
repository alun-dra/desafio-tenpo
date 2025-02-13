package com.example.backend_tenpo.controller;

import com.example.backend_tenpo.model.User;
import com.example.backend_tenpo.security.JwtUtil;
import com.example.backend_tenpo.service.UserService;
import com.example.backend_tenpo.dto.LoginRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class LoginController {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder; // 🔹 Ahora se inyecta en lugar de crearlo manualmente

    public LoginController(UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest) {
        Optional<User> user = userService.findByUsername(loginRequest.getUsername());

        if (user.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not found");
        }

        // System.out.println("Stored Password: " + user.get().getPassword()); // 🔹 Imprime la contraseña en la BD
        // System.out.println("Entered Password: " + loginRequest.getPassword()); // 🔹 Imprime la ingresada

        boolean passwordMatches = passwordEncoder.matches(loginRequest.getPassword(), user.get().getPassword());
        System.out.println("Password Match: " + passwordMatches); // 🔹 Verifica si la comparación funciona

        if (!passwordMatches) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }

        String token = jwtUtil.generateToken(user.get());

        return ResponseEntity.ok(Collections.singletonMap("token", token));
    }



}
