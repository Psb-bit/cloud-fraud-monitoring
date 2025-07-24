// Define the package this class belongs to
package com.example.fraud.service;

// Import the Transaction model class
import com.example.fraud.model.Transaction;

// Import the repository interface that connects to the database
import com.example.fraud.repository.TransactionRepository;

// Enables Spring to automatically inject dependencies
import org.springframework.beans.factory.annotation.Autowired;

// Marks this class as a Spring service component (used in the service layer)
import org.springframework.stereotype.Service;

// Import for Optional - used to avoid nulls when returning a single result
import java.util.Optional;

// Tell Spring this is a service class (business logic layer)
@Service
public class TransactionService {

    // This is our database interface — Spring will inject an instance of it
    private final TransactionRepository transactionRepository;

    // Constructor-based dependency injection
    @Autowired  // Spring will auto-wire the repository here
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // Save a new transaction to the database
    public Transaction saveTransaction(Transaction transaction) {
        // TODO: Add fraud detection logic here in future
        return transactionRepository.save(transaction); // Save to DB
    }

    // Fetch a transaction by its ID
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id); // May return empty if not found
    }

    // Fetch all transactions
    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll(); // Returns a list of all records
    }

    // Delete a transaction by ID
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id); // Remove from DB
    }
}
