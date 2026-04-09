# Axon Framework CQRS Lab

This is a sample project demonstrating CQRS (Command Query Responsibility Segregation) pattern using Axon Framework with Spring Boot.

## Overview

The project implements a simple patient registration system using CQRS principles:
- **Commands**: RegisterPatient
- **Events**: PatientRegistered
- **Aggregate**: PatientAggregate
- **Query**: Patient service with repository

## Technologies Used

- Java 17
- Spring Boot
- Axon Framework
- JPA/Hibernate
- H2 Database (for simplicity)
- Maven

## Project Structure

```
src/main/java/walter/example/lab/
├── LabApplication.java          # Main Spring Boot application
├── aggregate/
│   └── PatientAggregate.java    # CQRS Aggregate
├── command/
│   └── RegisterPatient.java     # Command class
├── config/
│   ├── AxonEventStoreConfig.java
│   ├── JpaTransactionConfig.java
│   └── MainDataSourceConfig.java
├── domain/controllers/
│   └── Patient.java             # REST Controller
├── en/
│   ├── Gender.java              # Enum
│   └── PatientRegistered.java   # Event class
├── repository/
│   ├── Patient.java             # Entity
│   └── PatientRepository.java   # JPA Repository
└── service/
    ├── EventDTO.java
    ├── PatientService.java
    ├── RegisterPatientDTO.java
    ├── ResponseDTO.java
    └── handler/
        └── PatientEventHandler.java
```

## How to Run

1. Clone the repository:
   ```bash
   git clone https://github.com/walterchipuriro/Axon-Framework.git
   cd Axon-Framework
   ```

2. Run with Maven:
   ```bash
   ./mvnw spring-boot:run
   ```

3. The application will start on `http://localhost:8080`

## API Endpoints

- `POST /patients` - Register a new patient
- `GET /patients` - Get all patients

## Configuration

Database and other configurations are in `src/main/resources/application.properties`.

## Testing

Run tests with:
```bash
./mvnw test
```

## Contributing

This is a lab project for learning CQRS with Axon Framework. Feel free to experiment and modify.