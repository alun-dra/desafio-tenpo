package com.example.backend_tenpo.controller;

import com.example.backend_tenpo.model.Transaction;
import com.example.backend_tenpo.service.TransactionService;
import com.example.backend_tenpo.security.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.security.core.Authentication;

import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.Page;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class) // ✅ Extender con Mockito
class TransactionControllerTest {

    private MockMvc mockMvc;

    @Mock // ✅ Mockear el servicio
    private TransactionService transactionService;

    @Mock // ✅ Mockear JwtUtil para evitar problemas de autenticación
    private JwtUtil jwtUtil;

    @InjectMocks // ✅ Inyectar los mocks en TransactionController
    private TransactionController transactionController;

    private Transaction transaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(transactionController).build();

        transaction = new Transaction();
        transaction.setAmount(5000);
        transaction.setMerchant("Supermercado");
        transaction.setTenpistaName("Juan Pérez");

        // ✅ Usamos `lenient()` para evitar la excepción UnnecessaryStubbingException
        lenient().when(jwtUtil.extractUsername(anyString())).thenReturn("testUser");

        // ✅ Mockear el Authentication y el SecurityContext
        Authentication authentication = Mockito.mock(Authentication.class);
        lenient().when(authentication.getName()).thenReturn("testUser");

        SecurityContext securityContext = Mockito.mock(SecurityContext.class);
        lenient().when(securityContext.getAuthentication()).thenReturn(authentication);

        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithMockUser(username = "testUser") // ✅ Simular un usuario autenticado
    void shouldCreateTransaction() throws Exception {
        when(transactionService.createTransaction(anyString(), any(Transaction.class)))
                .thenReturn(transaction);

        mockMvc.perform(post("/api/transactions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 5000, \"merchant\": \"Supermercado\", \"tenpistaName\": \"Juan Pérez\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(5000))
                .andExpect(jsonPath("$.merchant").value("Supermercado"));
    }

    @Test
    @WithMockUser(username = "testUser") // ✅ Simular un usuario autenticado
    void shouldGetUserTransactions() throws Exception {
        // ✅ Crear una lista de transacciones simulada
        List<Transaction> transactionList = Collections.singletonList(transaction);

        // ✅ Simular la respuesta del servicio como una página de transacciones correctamente
        Page<Transaction> transactionPage = new PageImpl<>(transactionList, PageRequest.of(0, 7), transactionList.size());
        when(transactionService.getUserTransactions(anyString(), anyInt(), anyInt())).thenReturn(transactionPage);

        mockMvc.perform(get("/api/transactions")
                .param("page", "0") // ✅ Asegurar los parámetros de paginación
                .param("size", "7")
                .header("Authorization", "Bearer fake-jwt-token") // ✅ Simular header Authorization
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk()) // ✅ Esperar un 200 OK
                .andExpect(jsonPath("$.content.length()").value(1)) // ✅ Validar contenido paginado
                .andExpect(jsonPath("$.content[0].merchant").value("Supermercado"));
    }




}
