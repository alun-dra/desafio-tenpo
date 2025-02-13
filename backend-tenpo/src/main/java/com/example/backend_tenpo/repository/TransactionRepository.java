package com.example.backend_tenpo.repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.example.backend_tenpo.model.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
    List<Transaction> findByUserId(Integer userId); // Método sin paginación

    // ✅ Método paginado asegurando que el ID sea Long
    Page<Transaction> findByUserId(Long userId, Pageable pageable);
}
