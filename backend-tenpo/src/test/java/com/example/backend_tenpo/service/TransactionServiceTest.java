package com.example.backend_tenpo.service;

import com.example.backend_tenpo.model.Transaction;
import com.example.backend_tenpo.model.User;
import com.example.backend_tenpo.repository.TransactionRepository;
import com.example.backend_tenpo.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransactionService transactionService;

    private User testUser;
    private Transaction testTransaction;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testUser = new User();
        testUser.setId(1L);
        testUser.setUsername("alunda");
        testUser.setEmail("alunda@example.com");
        testUser.setPassword("password");

        testTransaction = new Transaction();
        testTransaction.setId(1);
        testTransaction.setAmount(500);
        testTransaction.setMerchant("Supermercado");
        testTransaction.setTenpistaName("Juan Pérez");
        testTransaction.setDate(java.time.LocalDateTime.now());
        testTransaction.setUser(testUser);
    }

    @Test
    void shouldCreateTransaction() {
        when(userRepository.findByUsername("alunda")).thenReturn(Optional.of(testUser));
        when(transactionRepository.save(any(Transaction.class))).thenReturn(testTransaction);

        Transaction createdTransaction = transactionService.createTransaction("alunda", testTransaction);

        assertThat(createdTransaction).isNotNull();
        assertThat(createdTransaction.getUser().getUsername()).isEqualTo("alunda");
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }

    @Test
    void shouldGetUserTransactions() {
        // Configuración del usuario de prueba
        when(userRepository.findByUsername("alunda")).thenReturn(Optional.of(testUser));
    
        // Configuración de la transacción de prueba
        when(transactionRepository.findByUserId(eq(1L), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(testTransaction)));
    
        // Llamada al método del servicio con paginación (page = 0, size = 7)
        Page<Transaction> transactions = transactionService.getUserTransactions("alunda", 0, 7);
    
        // Verificaciones
        assertThat(transactions).isNotEmpty();
        assertThat(transactions.getContent().get(0).getMerchant()).isEqualTo("Supermercado");
    
        // Verifica que se llamó correctamente al repositorio con los parámetros adecuados
        verify(transactionRepository, times(1)).findByUserId(eq(1L), any(Pageable.class));
    }

}
