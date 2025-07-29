package com.example.fraud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.slf4j.LoggerFactory;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.Level;
// import javax.annotation.PostConstruct;

@SpringBootApplication  // Marks this as the main Spring Boot application class
public class FraudApplication {

    public static void main(String[] args) {
        //  @PostConstruct 
        // Logger kafkaLogger = (Logger) LoggerFactory.getLogger("org.apache.kafka");
        //     kafkaLogger.setLevel(Level.WARN);
        // Logger springKafkaLogger = (Logger) LoggerFactory.getLogger("org.springframework.kafka");
        //     springKafkaLogger.setLevel(Level.WARN);

        SpringApplication.run(FraudApplication.class, args);  // Bootstraps and starts the Spring application
    }
}
