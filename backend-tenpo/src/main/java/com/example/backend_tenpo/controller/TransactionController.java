package com.example.backend_tenpo.controller;

import com.example.backend_tenpo.model.Transaction;
import com.example.backend_tenpo.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Page;



@RestController
@RequestMapping("/api/transactions")
@Tag(name = "Transactions", description = "API para gestionar transacciones de los usuarios")
@SecurityRequirement(name = "BearerAuth")  // 🔹 Aplica el requisito de autenticación JWT a toda la clase
public class TransactionController {

    private final TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    /**
     * 📌 Crea una nueva transacción para el usuario autenticado.
     * 
     * @param transaction Datos de la transacción a registrar.
     * @return La transacción creada.
     */
    @Operation(
        summary = "Crear una nueva transacción",
        description = "Permite a un usuario autenticado registrar una nueva transacción.",
        security = @SecurityRequirement(name = "BearerAuth"), // ✅ Swagger incluirá el token en la petición
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción creada exitosamente",
                content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado (requiere token JWT)", content = @Content)
        }
    )
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@RequestBody Transaction transaction) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();  // 🔹 Obtiene el nombre de usuario desde el token
        return ResponseEntity.ok(transactionService.createTransaction(username, transaction));
    }

    /**
     * 📌 Obtiene todas las transacciones del usuario autenticado con paginación.
     *
     * @param page Número de página (inicia desde 0).
     * @param size Cantidad de elementos por página (por defecto, 7).
     * @return Lista paginada de transacciones del usuario autenticado.
     */
    @Operation(
        summary = "Obtener transacciones del usuario (paginadas)",
        description = "Recupera las transacciones registradas por el usuario autenticado con paginación.",
        security = @SecurityRequirement(name = "BearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Lista de transacciones obtenida",
                content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado (requiere token JWT)", content = @Content)
        }
    )
    
    @GetMapping
    public ResponseEntity<Page<Transaction>> getUserTransactions(
        @RequestHeader("Authorization") String token,
        @RequestParam(defaultValue = "0") int page,      // Página actual (por defecto 0)
        @RequestParam(defaultValue = "7") int size       // Tamaño de página (por defecto 7)
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();
        
        Page<Transaction> transactions = transactionService.getUserTransactions(username, page, size);
        return ResponseEntity.ok(transactions);
    }

    /**
     * 📌 Actualiza una transacción específica por su ID.
     * 
     * @param id          ID de la transacción a actualizar.
     * @param transaction Datos nuevos de la transacción.
     * @return La transacción actualizada.
     */
    @Operation(
        summary = "Actualizar una transacción",
        description = "Modifica una transacción específica del usuario autenticado.",
        security = @SecurityRequirement(name = "BearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción actualizada correctamente",
                content = @Content(schema = @Schema(implementation = Transaction.class))),
            @ApiResponse(responseCode = "401", description = "No autorizado (requiere token JWT)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada", content = @Content)
        }
    )
    @PutMapping("/{id}")
    public ResponseEntity<Transaction> updateTransaction(@PathVariable Integer id, @RequestBody Transaction transaction) {
        return ResponseEntity.ok(transactionService.updateTransaction(id, transaction));
    }

    /**
     * 📌 Elimina una transacción específica por su ID.
     * 
     * @param id    ID de la transacción a eliminar.
     * @return Mensaje de confirmación.
     */
    @Operation(
        summary = "Eliminar una transacción",
        description = "Elimina una transacción específica del usuario autenticado.",
        security = @SecurityRequirement(name = "BearerAuth"),
        responses = {
            @ApiResponse(responseCode = "200", description = "Transacción eliminada exitosamente"),
            @ApiResponse(responseCode = "401", description = "No autorizado (requiere token JWT)", content = @Content),
            @ApiResponse(responseCode = "404", description = "Transacción no encontrada", content = @Content)
        }
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteTransaction(@PathVariable Integer id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.ok("Transaction deleted");
    }
}
