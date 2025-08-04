# Cloud-fraud-monitoring

A backend system built with Java Spring Boot and AWS to detect and flag fraudulent transactions in real-time using rule-based logic. It integrates Kafka for scalable message streaming, MySQL (AWS RDS) for persistent storage, and Power BI for fraud analytics and reporting.

# Tech Stack

Java 17
Spring Boot
Kafka
MySQL (AWS RDS)
Docker
AWS (CloudWatch, RDS, EC2)
Power BI

# Features

✅ REST API to handle transaction data (GET, POST, DELETE)
✅ Real-time fraud detection using rule-based logic
✅ Kafka integration for transaction event streaming
✅ Power BI dashboard connected to live AWS RDS database
✅ Dockerized microservice architecture
✅ Postman collection for API testing

# Fraud Detection Rules

Transaction amount > $1000
Multiple transactions from same user in a short time (60 sec)
New location or device
First transaction is unusually large
Suspicious device pattern (e.g., "Chrome on Ubuntu Server")
[Optional future rule] Integration with ML-based detection

# Dashboard (Power BI)

Pie chart: Fraudulent vs Non-Fraudulent transactions
Filters by date, user ID, location
Auto-refreshes from AWS RDS
Future scope: Device-based drilldowns


# How to Run the Project (Dev Mode)
Step 1: Build the Spring Boot Application

./mvnw clean install

Step 2: Run the App and Kafka using Docker Compose

docker compose down        # Stop and remove previous containers (optional clean start)
docker compose up --build  # Build and run all services (Kafka, MySQL, Spring Boot)
