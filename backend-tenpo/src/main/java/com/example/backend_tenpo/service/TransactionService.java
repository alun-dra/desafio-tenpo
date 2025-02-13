package com.example.backend_tenpo.service;

import com.example.backend_tenpo.exception.ResourceNotFoundException;
import com.example.backend_tenpo.model.Transaction;
import com.example.backend_tenpo.model.User;
import com.example.backend_tenpo.repository.TransactionRepository;
import com.example.backend_tenpo.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;

    public TransactionService(TransactionRepository transactionRepository, UserRepository userRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
    }

    // ✅ Crear transacción con el usuario autenticado
    public Transaction createTransaction(String username, Transaction transaction) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        transaction.setUser(user);
        return transactionRepository.save(transaction);
    }

    // ✅ Obtener transacciones del usuario autenticado (paginadas)
    public Page<Transaction> getUserTransactions(String username, int page, int size) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));
    
        Pageable pageable = PageRequest.of(page, size);
        return transactionRepository.findByUserId(user.getId(), pageable); // ✅ Ahora funcionará
    }
    

    // ✅ Obtener una transacción por ID
    public Transaction getTransactionById(Integer id) {
        return transactionRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
    }

    // ✅ Actualizar una transacción
    public Transaction updateTransaction(Integer id, Transaction transaction) {
        return transactionRepository.findById(id).map(existing -> {
            existing.setAmount(transaction.getAmount());
            existing.setMerchant(transaction.getMerchant());
            existing.setTenpistaName(transaction.getTenpistaName());
            existing.setDate(transaction.getDate());
            return transactionRepository.save(existing);
        }).orElseThrow(() -> new ResourceNotFoundException("Transaction not found with ID: " + id));
    }

    // ✅ Eliminar una transacción
    public void deleteTransaction(Integer id) {
        if (!transactionRepository.existsById(id)) {
            throw new ResourceNotFoundException("Transaction not found with ID: " + id);
        }
        transactionRepository.deleteById(id);
    }
}
