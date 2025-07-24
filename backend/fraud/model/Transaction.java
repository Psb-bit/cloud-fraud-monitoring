package com.example.fraud.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

// This annotation tells Spring this class should be stored in the database
@Entity
// Sets the table name to "transactions" in the database
@Table(name = "transactions")
public class Transaction {

    // Primary key - unique ID for each transaction
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Auto-increment ID
    private Long id;

    // ID of the user who made the transaction
    @Column(nullable = false)
    private Long userId;

    // Transaction amount in dollars
    @Column(nullable = false)
    private Double amount;

    // Location (e.g., city or coordinates)
    private String location;

    // Device info (e.g., "iPhone", "Windows Chrome")
    private String device;

    // Timestamp when the transaction was made
    private LocalDateTime timestamp;

    // --- Getters & Setters ---

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDevice() {
        return device;
    }

    public void setDevice(String device) {
        this.device = device;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
}
