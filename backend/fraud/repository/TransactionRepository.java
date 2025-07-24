package com.example.fraud.repository;

 // Imports the Transaction entity (Java class mapped to DB table)
import com.example.fraud.model.Transaction;

// Provides built-in CRUD methods
import org.springframework.data.jpa.repository.JpaRepository;

// Marks this interface as a Spring-managed bean
import org.springframework.stereotype.Repository;

// This tells Spring to treat this interface as a Repository (DAO)
@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    // No need to write any code here for now
    // You already get save(), findById(), findAll(), delete(), etc.
//     You are not required to implement anything.

// Spring will automatically generate a proxy class at runtime with all standard methods like:

// save(Transaction t)

// findAll()

// findById(Integer id)

// deleteById(Integer id)

// These are inherited from JpaRepository.
}
