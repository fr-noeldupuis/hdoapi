# HDO API - Lourdes Pilgrimage Management System

A RESTful API backend for managing pilgrimage organizations to Lourdes. This application provides comprehensive tools for organizing and managing pilgrimage participants, events, and related administrative tasks.

## 🎯 Project Overview

The HDO API is designed to streamline the management of Lourdes pilgrimages by providing:
- **Participant Management**: Complete CRUD operations for pilgrimage participants
- **Pilgrimage Organization**: Tools for creating and managing pilgrimage events
- **Administrative Features**: Support for pilgrimage logistics and coordination

## 🛠 Technology Stack

- **Framework**: Spring Boot 3.5.4
- **Database**: PostgreSQL (planned), currently H2 for development
- **Java Version**: 17
- **Build Tool**: Maven
- **Additional Libraries**:
  - Spring Data JPA for database operations
  - Spring Data REST for RESTful endpoints
  - Lombok for reducing boilerplate code

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL (for production)

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hdoapi
   ```

2. **Build the project**
   ```bash
   ./mvnw clean install
   ```

3. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will start on `http://localhost:8080`

### Development Setup

For development, the application uses H2 in-memory database. The H2 console is available at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

## 📋 API Endpoints

Currently, the API is in initial development phase. The following endpoints are planned:

### Person Management
- `GET /api/persons` - List all participants
- `GET /api/persons/{id}` - Get participant details
- `POST /api/persons` - Create new participant
- `PUT /api/persons/{id}` - Update participant
- `DELETE /api/persons/{id}` - Remove participant

### Pilgrimage Management
- `GET /api/pilgrimages` - List all pilgrimages
- `GET /api/pilgrimages/{id}` - Get pilgrimage details
- `POST /api/pilgrimages` - Create new pilgrimage
- `PUT /api/pilgrimages/{id}` - Update pilgrimage
- `DELETE /api/pilgrimages/{id}` - Remove pilgrimage

## 🗺 Roadmap

### Phase 1: Core Features (Current)
- [ ] Set up PostgreSQL database configuration
- [ ] Implement Person entity and repository
- [ ] Create Person REST controller
- [ ] Implement Pilgrimage entity and repository
- [ ] Create Pilgrimage REST controller
- [ ] Basic CRUD operations for both entities

### Phase 2: Enhanced Features
- [ ] Authentication and authorization
- [ ] Participant registration system
- [ ] Pilgrimage enrollment management
- [ ] Payment tracking integration
- [ ] Document management (passports, medical forms)

### Phase 3: Advanced Features
- [ ] Group management and assignments
- [ ] Transportation and accommodation booking
- [ ] Medical information tracking
- [ ] Communication system (notifications, updates)
- [ ] Reporting and analytics dashboard

### Phase 4: Integration & Optimization
- [ ] Third-party service integrations
- [ ] Performance optimization
- [ ] Comprehensive testing suite
- [ ] API documentation with Swagger/OpenAPI
- [ ] Deployment automation

## 🏗 Project Structure

```
src/
├── main/
│   ├── java/fr/noeldupuis/hdoapi/
│   │   ├── HdoapiApplication.java          # Main application class
│   │   ├── controller/                     # REST controllers
│   │   ├── model/                          # Entity classes
│   │   ├── repository/                     # Data access layer
│   │   ├── service/                        # Business logic
│   │   └── config/                         # Configuration classes
│   └── resources/
│       ├── application.properties          # Application configuration
│       └── static/                         # Static resources
└── test/                                   # Test files
```

## 🔧 Configuration

The application configuration is managed through `application.properties`. Key configurations include:

- Database connection settings
- Server port configuration
- JPA/Hibernate settings
- Logging configuration

## 🧪 Testing

Run tests using Maven:
```bash
./mvnw test
```

## 📝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🤝 Support

For support and questions, please contact the development team or create an issue in the repository.

---

**Note**: This project is currently in active development. Features and endpoints are being implemented according to the roadmap above.
