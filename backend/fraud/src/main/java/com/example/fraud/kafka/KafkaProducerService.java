// Package declaration: this places the class inside the kafka folder package
package com.example.fraud.kafka;


// Import statements: allow usage of other classes without full package path
import com.example.fraud.model.Transaction;           // Transaction model class
import org.springframework.beans.factory.annotation.Autowired;  // For dependency injection annotation
import org.springframework.kafka.core.KafkaTemplate;            // Kafka template for sending messages
import org.springframework.stereotype.Service;                   // Marks this class as a service component
import java.util.concurrent.TimeUnit;

/**
 * The @Service annotation registers this class as a Spring service component.
 * It indicates this class contains business logic and is eligible for component scanning.
 */
@Service
public class KafkaProducerService {

    // Declaring a private final field for KafkaTemplate.
    // KafkaTemplate handles sending messages to Kafka topics.
    // 'final' means this variable can only be assigned once (immutability).
    private final KafkaTemplate<String, Transaction> kafkaTemplate;

    // Constructor for KafkaProducerService.
    // The @Autowired annotation tells Spring to inject an instance of KafkaTemplate when this class is created.
    // Constructor injection is a recommended way to provide dependencies in Spring.
    @Autowired
    public KafkaProducerService(KafkaTemplate<String, Transaction> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;  // Assign injected KafkaTemplate to the class field
    }

    /**
     * This method publishes a Transaction message to a Kafka topic.
     * 
     * @param topic The Kafka topic name where the message will be sent.
     * @param transaction The Transaction object to be sent as message payload.
     */
    public void sendTransaction(String topic, Transaction transaction) {
        // kafkaTemplate.send() sends a message asynchronously to the given topic.
        // The first argument is the topic name (String),
        // the second argument is the payload (Transaction object).
        //    try {
        kafkaTemplate.send("transactions-topic", transaction);
    // } catch (Exception e) {
    //     // Log error and rethrow or handle accordingly
    //     log.error("Failed to send transaction to Kafka", e);
    //     throw new KafkaException("Kafka send failed", e);
    // }
        // Print a message to the console for logging/debugging.
        // System.out.println is a standard way to print output to the console.
        System.out.println("Sent message to topic " + topic + ": " + transaction.toString());
    }
}
