// ------------------------------
// Package Declaration
// ------------------------------
// The `package` keyword is used to define the namespace of this class.
// It's like a folder structure to organize Java classes.
// Here, this class belongs to the 'com.example.fraud.service' package.
package com.example.fraud.service;

// ------------------------------
// Import Statements
// ------------------------------
// `import` allows you to use classes from other packages without writing their full path.

// Import the Transaction class from your model package.
import com.example.fraud.model.Transaction;

// Import the TransactionRepository interface which handles DB access.
import com.example.fraud.repository.TransactionRepository;

// Allows Spring to inject the repository dependency automatically using constructor injection.
import org.springframework.beans.factory.annotation.Autowired;

// Marks this class as a Spring service, so it is detected during component scanning.
import org.springframework.stereotype.Service;

// Java standard library imports:
import java.util.List;           // Interface for a list of objects (like an array that grows)
import java.util.ArrayList;      // Implementation of the List interface
import java.util.Optional;       // Wrapper class to avoid null pointer exceptions
import java.time.Duration;       // Represents a time-based amount (e.g. 60 seconds)
import java.time.LocalDateTime;  // Represents date and time (e.g. 2025-07-26T14:00)

// -------------------------------------
// Service class declaration
// -------------------------------------

/**
 * `@Service` annotation tells Spring that this class contains business logic.
 * It is a specialized version of the `@Component` annotation.
 */
@Service
public class TransactionService {

    // -------------------------------------
    // Dependency Injection
    // -------------------------------------

    // `private` means this variable is only accessible inside this class.
    // `final` means the reference cannot be reassigned after it's set once.
    private final TransactionRepository transactionRepository;

    /**
     * Constructor-based dependency injection.
     * `@Autowired` tells Spring to automatically provide a `TransactionRepository` instance.
     * This is better than field injection and promotes immutability.
     */
    @Autowired
    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    // -------------------------------------
    // Method to Save a Transaction
    // -------------------------------------

    /**
     * Saves a transaction to the database and checks for fraud before saving.
     * @param transaction - the transaction to be saved
     * @return the transaction with fraud detection results
     */
    public Transaction saveTransaction(Transaction transaction) {

        // `getDevice()` gets the device string from the Transaction object.
        // `!= null` checks if the string exists to avoid NullPointerException.
        // `trim()` removes leading/trailing spaces; `toLowerCase()` makes it case-insensitive.
        if (transaction.getDevice() != null) {
            transaction.setDevice(transaction.getDevice().trim().toLowerCase());
        }

        // Normalize location string in the same way.
        if (transaction.getLocation() != null) {
            transaction.setLocation(transaction.getLocation().trim().toLowerCase());
        }

        // Retrieves all past transactions for the given user ID.
        List<Transaction> previousTransactions = transactionRepository.findByUserId(transaction.getUserId());

        // Calls method to apply all 6 fraud detection rules.
        List<String> fraudReasons = detectFraud(transaction, previousTransactions);

        // If fraud is detected (list is not empty), mark as fraudulent.
        if (!fraudReasons.isEmpty()) {
            transaction.setIsFraudulent(true);

            // Join all fraud reasons with commas and save them to the DB.
            transaction.setFraudReasons(String.join(", ", fraudReasons));
        } else {
            transaction.setIsFraudulent(false);
            transaction.setFraudReasons("All checks passed, no fraud detected");
        }

        // Save transaction to the database using the repository's save method.
        Transaction saved = transactionRepository.save(transaction);

        // Return the saved transaction to the caller (e.g. controller).
        return saved;
    }

    // -------------------------------------
    // Method to Detect Fraud Rules
    // -------------------------------------

    private List<String> detectFraud(Transaction transaction, List<Transaction> previousTransactions) {
        // Create an empty list to collect fraud reasons.
        List<String> fraudReasons = new ArrayList<>();

        // ---------------------------
        // Rule 1: Amount > $1000
        // ---------------------------
        if (transaction.getAmount() != null && transaction.getAmount() > 1000) {
            fraudReasons.add("Amount exceeds $1000");
        }

        // ---------------------------
        // Rule 2: Multiple transactions within 60 seconds
        // ---------------------------

        // `System.out.println()` is used to print messages to the console (stdout) for debugging.
        //System.out.println("Current transaction timestamp: " + transaction.getTimestamp());
        //System.out.println("Looking for last transaction before this timestamp for userId: " + transaction.getUserId());

        // If timestamp is null, set it to the current date-time.
        if (transaction.getTimestamp() == null) {
            transaction.setTimestamp(LocalDateTime.now()); // Assigns current system time
        }

        // Finds the most recent transaction for the same user before this one.
        // `Optional` is used to avoid `null` results. `isPresent()` checks if value exists.
        Optional<Transaction> lastTransactionOpt =
            transactionRepository.findTopByUserIdAndTimestampBeforeOrderByTimestampDesc(
                transaction.getUserId(),
                transaction.getTimestamp()
            );

        //System.out.println("Found previous transaction? " + lastTransactionOpt.isPresent());

        // If a previous transaction exists...
        if (lastTransactionOpt.isPresent()) {

            // `get()` retrieves the value from the Optional wrapper.
            Transaction lastTransaction = lastTransactionOpt.get();

            // Log timestamp of last transaction
           // System.out.println("Last transaction timestamp: " + lastTransaction.getTimestamp());

            // Ensure both timestamps are not null
            if (lastTransaction.getTimestamp() != null && transaction.getTimestamp() != null) {

                // `Duration.between()` calculates the time difference.
                // `getSeconds()` converts it into seconds.
                long seconds = Math.abs(Duration.between(transaction.getTimestamp(), lastTransaction.getTimestamp()).getSeconds());

                //System.out.println("Comparing timestamps: " + transaction.getTimestamp() + " and " + lastTransaction.getTimestamp());

                // If difference is under or equal to 60 seconds, flag as fraud.
                if (seconds <= 60) {
                    //System.out.println("Multiple transactions detected within 60 seconds" + seconds);
                    fraudReasons.add("Multiple transactions in a short time");
                }
            }
        }

        // ---------------------------
        // Rule 3: Location not seen before
        // ---------------------------

        // Normalize and lower-case location
        String currentLocation = transaction.getLocation() == null ? "" : transaction.getLocation().trim().toLowerCase();

        // Check if this location was ever used by the user
        boolean locationSeenBefore = previousTransactions.stream().anyMatch(t -> {
            String pastLocation = t.getLocation() == null ? "" : t.getLocation().trim().toLowerCase();
            return pastLocation.equals(currentLocation);
        });

        // If not seen before and not empty, flag it
        if (!locationSeenBefore && !currentLocation.isEmpty()) {
            fraudReasons.add("Transaction from new location");
        }

        // ---------------------------
        // Rule 4: New device not seen before
        // ---------------------------

        // Normalize device string
        String currentDevice = transaction.getDevice() == null ? "" : transaction.getDevice().trim().toLowerCase();

        // Check if device was used before
        boolean deviceSeenBefore = previousTransactions.stream().anyMatch(t -> {
            String pastDevice = t.getDevice() == null ? "" : t.getDevice().trim().toLowerCase();
            return pastDevice.equals(currentDevice);
        });

        if (!deviceSeenBefore && !currentDevice.isEmpty()) {
            fraudReasons.add("Transaction from new device");
        }

        // ---------------------------
        // Rule 5: First transaction unusually large
        // ---------------------------
        if (previousTransactions.isEmpty() && transaction.getAmount() != null && transaction.getAmount() > 500) {
            fraudReasons.add("First transaction is unusually large");
        }

        // ---------------------------
        // Rule 6: Suspicious device name
        // ---------------------------
        if (transaction.getDevice() != null && transaction.getDevice().toLowerCase().contains("ubuntu server")) {
            fraudReasons.add("Suspicious device pattern: " + transaction.getDevice());
        }

        // Return list of fraud reasons (can be empty)
        return fraudReasons;
    }

    // -------------------------------------
    // Get transaction by ID
    // -------------------------------------

    public Optional<Transaction> getTransactionById(Long id) {
        // Returns a transaction wrapped in Optional (could be empty if ID doesn't exist)
        return transactionRepository.findById(id);
    }

    // -------------------------------------
    // Get all transactions
    // -------------------------------------

    public Iterable<Transaction> getAllTransactions() {
        // Returns all transaction entries in the database.
        return transactionRepository.findAll();
    }

    // -------------------------------------
    // Delete a transaction by ID
    // -------------------------------------

    public void deleteTransaction(Long id) {
        // Deletes a transaction from DB using its ID.
        transactionRepository.deleteById(id);
    }
}
