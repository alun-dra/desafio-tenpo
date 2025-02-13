package com.example.backend_tenpo.repository;

import com.example.backend_tenpo.model.Transaction;
import com.example.backend_tenpo.model.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;


import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@ActiveProfiles("test")  // Usa el perfil de H2 para pruebas
public class TransactionRepositoryTest {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void shouldFindTransactionsByUserId() {
        // Crear un usuario para la transacción
        User user = new User();
        user.setUsername("testUser");
        user.setEmail("test@example.com");
        user.setPassword("hashedpassword");
        user = userRepository.save(user);

        // Crear una transacción asociada al usuario
        Transaction transaction = new Transaction();
        transaction.setUser(user);
        transaction.setAmount(5000);
        transaction.setMerchant("Supermercado");
        transaction.setTenpistaName("Juan Pérez");
        transaction.setDate(LocalDateTime.now());

        transactionRepository.save(transaction);

        // Agregar un objeto Pageable
        Pageable pageable = PageRequest.of(0, 7); // Página 0, 7 elementos por página

        // Obtener transacciones del usuario con paginación
        Page<Transaction> transactions = transactionRepository.findByUserId(user.getId(), pageable);

        // Validar que la lista no está vacía y contiene la transacción correcta
        assertThat(transactions.getContent()).isNotEmpty();
        assertThat(transactions.getContent().size()).isEqualTo(1);
        assertThat(transactions.getContent().get(0).getMerchant()).isEqualTo("Supermercado");
        assertThat(transactions.getContent().get(0).getAmount()).isEqualTo(5000);
    }
}
