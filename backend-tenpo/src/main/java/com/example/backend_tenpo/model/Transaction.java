package com.example.backend_tenpo.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "transactions")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id; // ID transacción

    @NotNull(message = "Transaction amount is required")
    private Integer amount; // Monto transacción en pesos

    @NotBlank(message = "Merchant is required")
    private String merchant; // Giro o comercio de transacción

    @NotBlank(message = "Tenpista name is required")
    private String tenpistaName; // Nombre de Tenpista

    @NotNull(message = "Transaction date is required")
    private LocalDateTime date; // Fecha de transacción

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonIgnore
    private User user; // Relación con usuario
}
