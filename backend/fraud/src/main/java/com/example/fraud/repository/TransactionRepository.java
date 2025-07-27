package com.example.fraud.repository;

// Import the Transaction entity class which maps to the DB table
import com.example.fraud.model.Transaction;

// Import JpaRepository which provides built-in CRUD methods
import org.springframework.data.jpa.repository.JpaRepository;

// Marks this interface as a Spring-managed bean (optional but recommended)
import org.springframework.stereotype.Repository;

// Import List for return type
import java.util.List;

import java.time.LocalDateTime;  // For LocalDateTime type
import java.util.Optional;       // For Optional return type


// This annotation marks this interface as a repository component
@Repository
// Define the repository interface that extends JpaRepository for Transaction entities with primary key type Long
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    // Declare a custom query method to find all Transactions by userId
    // Spring Data JPA will automatically generate the implementation based on method name
    List<Transaction> findByUserId(Long userId);
boolean existsByUserIdAndDeviceAndLocation(Long userId, String device, String location);

Optional<Transaction> findTopByUserIdAndTimestampBeforeOrderByTimestampDesc(Long userId, LocalDateTime timestamp);

    // Note:
    // The method name must follow Spring Data JPA conventions to generate correct query.
    // This method will generate SQL similar to:
    // SELECT * FROM transactions WHERE user_id = ?
}
