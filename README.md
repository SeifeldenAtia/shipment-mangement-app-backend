# Shipment Application

A Spring Boot application for managing shipments with real-time updates via WebSocket and REST API endpoints.

## Features

- **Shipment Management**: Create, read, update, and delete shipments
- **Real-time Updates**: WebSocket support for live shipment status notifications
- **CORS Enabled**: Cross-origin requests support for frontend integration
- **Error Handling**: Comprehensive exception handling with meaningful error responses
- **RESTful API**: Clean and intuitive REST API endpoints
- **Database Persistence**: JPA/Hibernate for data persistence

## Technology Stack

- **Java 11+**: Programming language
- **Spring Boot 2.x**: Web framework
- **Spring Data JPA**: Data access layer
- **Spring WebSocket**: Real-time communication
- **Maven**: Build and dependency management
- **Database**: Configured via application.properties

## Prerequisites

- Java Development Kit (JDK) 11 or higher
- Maven 3.6+
- Git (for version control)

## Installation & Setup

1. **Clone the repository**

   ```bash
   git clone https://github.com/SeifeldenAtia/shipment-mangement-app-backend.git
   cd shipment
   ```

2. **Configure the database**
    - Edit `src/main/resources/application.properties`
    - Set up your database connection details (URL, username, password)

3. **Build the project**
   ```bash
   mvn clean install
   ```

## Running the Application

### Using Maven

```bash
mvn spring-boot:run
```

### Using Java

```bash
java -jar target/shipmentApp-0.0.1-SNAPSHOT.jar
```

The application will start on `http://localhost:8080` by default.

## Project Structure

```
src/main/java/com/example/shipmentApp/
├── ShipmentApplication.java          # Main Spring Boot application class
├── config/
│   ├── CorsConfig.java              # CORS configuration
│   └── WebSocketConfig.java         # WebSocket configuration
├── shipment/
│   ├── Shipment.java                # Entity class
│   ├── ShipmentDTO.java             # Data Transfer Object
│   ├── ShipmentController.java      # REST API endpoints
│   ├── ShipmentService.java         # Business logic
│   ├── ShipmentRepository.java      # Data access layer
│   ├── ShipmentStatus.java          # Shipment status enumeration
│   └── error/
│       ├── ErrorResponseDTO.java    # Standard error response
│       ├── GlobalExceptionHandler.java  # Global exception handling
│       └── ShipmentNotFoundException.java # Custom exception
└── resources/
    └── application.properties        # Application configuration
```

## Configuration

Edit `src/main/resources/application.properties` to configure:

- **Server Port**: `server.port` (default: 8080)
- **Database Connection**:
    - `spring.datasource.url`
    - `spring.datasource.username`
    - `spring.datasource.password`
- **JPA/Hibernate**:
    - `spring.jpa.hibernate.ddl-auto`
    - `spring.jpa.show-sql`

Example:

```properties
server.port=8080
spring.application.name=shipmentApp
spring.datasource.url=jdbc:mysql://localhost:3306/shipmentdb
spring.datasource.username=root
spring.datasource.password=your_password
spring.jpa.hibernate.ddl-auto=update
```

## API Endpoints

- **GET** `/api/shipments` - Get all shipments
- **GET** `/api/shipments/{id}` - Get a specific shipment
- **POST** `/api/shipments` - Create a new shipment
- **PUT** `/api/shipments/{id}` - Update a shipment
- **DELETE** `/api/shipments/{id}` - Delete a shipment

## WebSocket Events

WebSocket endpoints are configured for real-time shipment updates:

- **Connect**: `/ws/shipments`
- **Messages**: Subscriptions to shipment status changes

## Error Handling

The application provides standardized error responses through the `GlobalExceptionHandler`. Common error scenarios:

- **404 Not Found**: Shipment not found (ShipmentNotFoundException)
- **400 Bad Request**: Invalid request parameters
- **500 Internal Server Error**: Unexpected server errors

Error Response Format:

```json
{
  "message": "Shipment not found",
  "status": 404,
  "timestamp": "2026-05-17T10:30:00Z"
}
```

## Testing

Run the test suite:

```bash
mvn test
```

## Build Output

The compiled application can be found in the `target/` directory after building.

## Contributing

1. Create a feature branch (`git checkout -b feature/AmazingFeature`)
2. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
3. Push to the branch (`git push origin feature/AmazingFeature`)
4. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Support

For issues and questions, please open an issue in the repository or contact the development team.

---

**Happy Shipping!** 📦
