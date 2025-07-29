// Package declaration: this places the class inside the kafka folder package
package com.example.fraud.kafka;

// Import statements to use other classes without fully qualifying their names
import com.example.fraud.model.Transaction;               // Our data model
import org.springframework.kafka.annotation.KafkaListener; // Annotation to mark methods as Kafka message listeners
import org.springframework.stereotype.Service;             // Marks this class as a Spring service component

/**
 * The @Service annotation marks this class as a Spring-managed service component.
 * This class will consume messages from Kafka topics.
 */
@Service
public class KafkaConsumerService {

    /**
     * The @KafkaListener annotation marks this method to listen to messages
     * from the specified Kafka topic(s). When a message arrives, this method is called.
     * 
     * @param transaction The consumed Transaction message from Kafka.
     */
    @KafkaListener(topics = "transactions-topic", groupId = "fraud-monitoring-group")
    public void consumeMessage(Transaction transaction) {
        // This method is called automatically when a new message is received on "transactions-topic".
        // It receives the deserialized Transaction object from Kafka.

        // Print the received transaction to the console for monitoring/debugging.
        System.out.println("Received message from Kafka: " + transaction);

        // Here you can add logic to process the transaction message,
        // e.g., validate it, check for fraud, store it, or trigger other workflows.
    }
}
