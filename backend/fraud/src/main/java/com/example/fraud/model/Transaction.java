// Package declaration groups this file into the folder structure for this app
package com.example.fraud.model;

// --- Import Java Persistence API (JPA) annotations to map this class to a DB table ---
import jakarta.persistence.*;

// Import date-time class for transaction timestamp
import java.time.LocalDateTime;

// --- Import Jakarta Validation Annotations ---
// These are used to validate user input (via REST API, form, etc.)
import jakarta.validation.constraints.*;

// --- Main Entity Class ---
@Entity  // Tells JPA this class is a DB table
@Table(name = "transactions")  // Sets the actual DB table name to "transactions"
public class Transaction {

    // --- PRIMARY KEY CONFIGURATION ---
    
    @Id  // Declares this field as the table's Primary Key
    @GeneratedValue(strategy = GenerationType.IDENTITY)  
    // IDENTITY = Database auto-generates incrementing ID (e.g., 1, 2, 3, ...)
    private Long id;  // Field to store unique identifier for each transaction

    // --- VALIDATED USER ID ---
    
    @NotNull(message = "User ID is required")  // Validation: Must not be null
    @Column(nullable = false)  // DB constraint: cannot be null
    private Long userId;  // Field for the user performing the transaction

    // --- VALIDATED AMOUNT ---
    
    @NotNull(message = "Amount is required")  // Validation: Must not be null
    @Positive(message = "Amount must be positive")  // Validation: Must be > 0 (no negatives)
    @Column(nullable = false)  // DB: cannot be null
    private Double amount;  // The dollar amount of the transaction

    // --- OPTIONAL LOCATION FIELD ---
    @NotBlank(message = "Location is required")  
    @Size(max = 255, message = "Location can be at most 255 characters")
    // Optional: limits the string length for safety
    private String location;  // Location string (e.g. city, region)

    // --- OPTIONAL DEVICE FIELD ---
    @NotBlank(message = "Device info is required")  
    @Size(max = 255, message = "Device info can be at most 255 characters")
    private String device;  // Device used to perform the transaction

    // --- TIMESTAMP HANDLED AUTOMATICALLY ---
    
    private LocalDateTime timestamp;  // Date/time of transaction (set automatically)

    // --- FRAUD FLAG ---
    
    @Column(name = "is_fraudulent", nullable = false)  // DB column for fraud flag (true/false)
    private boolean isFraudulent = false;  // By default, assume no fraud

    // --- FRAUD REASONS STRING ---
    
    @Column(name = "fraud_reasons", length = 1000)  // Limit to 1000 characters in DB
    private String fraudReasons;  // Reason(s) why flagged as fraud

    // --- JPA Lifecycle Callback ---
    
    @PrePersist  // This method runs just before saving the record to the DB
    protected void onCreate() {
        this.timestamp = LocalDateTime.now();  // Auto-set timestamp to current time
        System.out.println("Transaction created at: " + this.timestamp);  // Optional log
    }

    // === GETTERS & SETTERS (Standard Java methods for encapsulation) ===

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;  // Usually not set manually, because DB generates this
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;  // Set the user ID
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;  // Set the transaction amount
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;  // Set the location value
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;  // Set the device value
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;  // Set timestamp manually (usually not needed)
    }

    public boolean isFraudulent() {
        return isFraudulent;
    }

    public void setIsFraudulent(boolean isFraudulent) {
        this.isFraudulent = isFraudulent;
    }

    public String getFraudReasons() {
        return fraudReasons;
    }

    public void setFraudReasons(String fraudReasons) {
        this.fraudReasons = fraudReasons;
    }
}
