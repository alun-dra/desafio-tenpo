package com.example.backend_tenpo.service;

import com.example.backend_tenpo.model.User;
import com.example.backend_tenpo.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public User registerUser(User user) {
        System.out.println("Raw Password Before Encoding: " + user.getPassword()); // ✅ Debe imprimir "240500"
        
        // 🔹 Verifica si la contraseña ya está encriptada antes de guardarla
        if (!user.getPassword().startsWith("$2a$")) { 
            user.setPassword(passwordEncoder.encode(user.getPassword())); // 🔹 Encripta solo si no está encriptada
        }
    
        System.out.println("Encoded Password: " + user.getPassword()); // ✅ Debe imprimir algo como "$2a$10$..."
        
        return userRepository.save(user);
    }
    
    
    
}