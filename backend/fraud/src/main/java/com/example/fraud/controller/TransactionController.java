// Expose APIs via a Controller — this is how other services (or a frontend) can send transactions, fetch history, etc.


// Defines the package structure of this controller (helps organize code)
package com.example.fraud.controller;

// Import the Transaction model class (used for request/response and service methods)
import com.example.fraud.model.Transaction;

// Import the TransactionService class to delegate business logic
import com.example.fraud.service.TransactionService;

// Allows Spring to automatically inject (autowire) dependencies into this class
import org.springframework.beans.factory.annotation.Autowired;

// Used to create standard HTTP responses like 200 OK, 404 Not Found, etc.
import org.springframework.http.ResponseEntity;

// Used to define this class as a RESTful controller so it can handle web requests
import org.springframework.web.bind.annotation.*;

// Optional is used for safely handling the case where a transaction may not be found by ID
import java.util.Optional;

import jakarta.validation.Valid;


// Mark this class as a REST controller so Spring Boot can expose it via HTTP
@RestController
// Base URL for all endpoints in this class: http://localhost:8080/api/transactions
@RequestMapping("/api/transactions")
public class TransactionController {

    // The service layer that contains business logic
    private final TransactionService transactionService;

    // Constructor-based injection to initialize the service
    @Autowired
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    // POST /api/transactions
    // This endpoint creates a new transaction using the JSON body provided
    @PostMapping
    public ResponseEntity<Transaction> createTransaction(@Valid @RequestBody Transaction transaction) {
        // Save the transaction using the service
        Transaction saved = transactionService.saveTransaction(transaction);
        // Return the saved object as JSON with HTTP 200 OK
        return ResponseEntity.ok(saved);
    }

    // GET /api/transactions/{id}
    // This endpoint fetches a specific transaction by its ID
    @GetMapping("/{id}")
    public ResponseEntity<Transaction> getTransaction(@PathVariable Long id) {
        // Ask the service for the transaction
        Optional<Transaction> transaction = transactionService.getTransactionById(id);
        // If found, return it with 200 OK; otherwise return 404 Not Found
        return transaction.map(ResponseEntity::ok)
                          .orElse(ResponseEntity.notFound().build());
    }

    // GET /api/transactions
    // This endpoint returns all transactions in the system
    @GetMapping
    public ResponseEntity<Iterable<Transaction>> getAllTransactions() {
        // Call service to get all transactions and return them
        return ResponseEntity.ok(transactionService.getAllTransactions());
    }

    // DELETE /api/transactions/{id}
    // This endpoint deletes a transaction by its ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        // Call the service to delete the transaction
        transactionService.deleteTransaction(id);
        // Return 204 No Content to indicate success without any body
        return ResponseEntity.noContent().build();
    }
}
