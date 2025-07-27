// Package declaration:
// Groups this class logically in the folder structure "com.example.fraud.model"
package com.example.fraud.model;

// Import statements:
// Bring in Jakarta Persistence API (JPA) annotations and classes to map this Java class to a DB table
import jakarta.persistence.*;

// Import LocalDateTime class to handle date and time values
import java.time.LocalDateTime;

/**
 * Entity class declaration:
 * @Entity tells JPA this class should be mapped to a database table.
 * @Table specifies the exact table name in the database ("transactions").
 * This class represents one row in the "transactions" table.
 */
@Entity
@Table(name = "transactions")
public class Transaction {

    /**
     * Field declarations:
     * Each private variable corresponds to a column in the database table.
     * Access is encapsulated via getters and setters.
     */

    // @Id marks this field as the Primary Key of the table
    @Id
    // @GeneratedValue configures how the primary key is generated.
    // IDENTITY means the database automatically assigns a unique auto-incremented value.
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // Unique identifier for each transaction record
    private Long id;

    // @Column configures the properties of the database column:
    // nullable = false means this column cannot be null
    @Column(nullable = false)
    // User ID who performed the transaction
    private Long userId;

    // Amount field mapped to a non-nullable column
    @Column(nullable = false)
    // Stores transaction amount (e.g., in USD)
    private Double amount;

    // Location field (city, region, etc.), no explicit @Column means default mapping,
    // nullable by default (can be null)
    private String location;

    // Device field (browser, OS info, phone, etc.), nullable by default
    private String device;

    // Timestamp field to store date/time when transaction occurred
    private LocalDateTime timestamp;

    // Custom column name "is_fraudulent" mapped to this field,
    // not nullable (must have value true or false)
    @Column(name = "is_fraudulent", nullable = false)
    // Boolean flag indicating fraud status, default is false
    private boolean isFraudulent = false;

    // Added: fraudReasons field to store reasons (comma-separated) why transaction flagged fraudulent
    @Column(name = "fraud_reasons", length = 1000)  // Optional length limit for DB column
    private String fraudReasons;

    /**
     * JPA lifecycle callback method:
     * @PrePersist annotation means this method runs automatically BEFORE
     * the entity is saved to the database for the first time.
     */
    @PrePersist
    protected void onCreate() {
        // Set the timestamp field to current date and time
        this.timestamp = LocalDateTime.now();
        // Optional debug print to console when a transaction is created
        System.out.println("Transaction created at: " + this.timestamp);
    }

    // --- Getters and Setters ---

    /**
     * Getter for id field:
     * 'public' means accessible from any other class.
     * Returns the value of private variable 'id'.
     */
    public Long getId() {
        return id;
    }

    /**
     * Setter for id field:
     * Takes a Long parameter and assigns it to 'id'.
     * Usually, this is auto-generated and not set manually.
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Getter for userId:
     * Returns the user ID linked to this transaction.
     */
    public Long getUserId() {
        return userId;
    }

    /**
     * Setter for userId:
     * Sets the user ID field with the given Long value.
     */
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    /**
     * Getter for amount:
     * Returns the transaction amount.
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * Setter for amount:
     * Sets the amount field.
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

    /**
     * Getter for location:
     * Returns the location string.
     */
    public String getLocation() {
        return location;
    }

    /**
     * Setter for location:
     * Sets the location field.
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * Getter for device:
     * Returns the device info string.
     */
    public String getDevice() {
        return device;
    }

    /**
     * Setter for device:
     * Sets the device field.
     */
    public void setDevice(String device) {
        this.device = device;
    }

    /**
     * Getter for timestamp:
     * Returns the timestamp of when the transaction was made.
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    /**
     * Setter for timestamp:
     * Allows manually setting the timestamp.
     * Usually handled by @PrePersist lifecycle method automatically.
     */
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    /**
     * Getter for isFraudulent:
     * Boolean method naming convention for flags uses 'is' prefix.
     * Returns true if transaction is flagged as fraud.
     */
    public boolean isFraudulent() {
        return isFraudulent;
    }

    /**
     * Setter for isFraudulent:
     * Sets fraud flag to true or false.
     */
    public void setIsFraudulent(boolean isFraudulent) {
        this.isFraudulent = isFraudulent;
    }

    /**
     * Getter for fraudReasons:
     * Returns the comma-separated fraud reasons string.
     */
    public String getFraudReasons() {
        return fraudReasons;
    }

    /**
     * Setter for fraudReasons:
     * Sets the fraud reasons string.
     */
    public void setFraudReasons(String fraudReasons) {
        this.fraudReasons = fraudReasons;
    }
}
