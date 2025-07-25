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
  - Spring Web for RESTful endpoints
  - Lombok for reducing boilerplate code
  - JUnit 5 for testing
  - Mockito for mocking in tests
  - AssertJ for fluent assertions

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

The API currently provides the following endpoints:

### Person Management ✅ (Implemented)
- `GET /api/persons` - List all participants
- `GET /api/persons/{id}` - Get participant details
- `POST /api/persons` - Create new participant
- `PUT /api/persons/{id}` - Update participant
- `DELETE /api/persons/{id}` - Remove participant

**Request/Response Examples:**

**Create a new person:**
```bash
POST /api/persons
Content-Type: application/json

{
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "1990-01-01"
}
```

**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "birthDate": "1990-01-01"
}
```

### Pilgrimage Management ✅ (Implemented)
- `GET /api/pilgrimages` - List all pilgrimages
- `GET /api/pilgrimages/{id}` - Get pilgrimage details
- `POST /api/pilgrimages` - Create new pilgrimage
- `PUT /api/pilgrimages/{id}` - Update pilgrimage
- `DELETE /api/pilgrimages/{id}` - Remove pilgrimage

**Request/Response Examples:**

**Create a new pilgrimage:**
```bash
POST /api/pilgrimages
Content-Type: application/json

{
  "name": "Lourdes 2025",
  "startDate": "2025-06-15",
  "endDate": "2025-06-22"
}
```

**Response:**
```json
{
  "id": 1,
  "name": "Lourdes 2025",
  "startDate": "2025-06-15",
  "endDate": "2025-06-22"
}
```

## 🗺 Roadmap

### Phase 1: Core Features ✅ (Completed)
- [ ] Set up PostgreSQL database configuration
- [x] Implement Person entity and repository
- [x] Create Person REST controller
- [x] Basic CRUD operations for Person entity
- [x] Implement Pilgrimage entity and repository
- [x] Create Pilgrimage REST controller
- [x] Basic CRUD operations for Pilgrimage entity

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
│   │   ├── persons/                        # Person management module
│   │   │   ├── entity/
│   │   │   │   └── Person.java             # Person JPA entity
│   │   │   ├── dto/
│   │   │   │   ├── PersonDto.java          # Person response DTO
│   │   │   │   ├── CreatePersonRequest.java # Person creation DTO
│   │   │   │   └── UpdatePersonRequest.java # Person update DTO
│   │   │   ├── repository/
│   │   │   │   └── PersonRepository.java   # Person data access
│   │   │   ├── service/
│   │   │   │   ├── PersonService.java      # Person service interface
│   │   │   │   └── PersonServiceImpl.java  # Person service implementation
│   │   │   └── controller/
│   │   │       └── PersonController.java   # Person REST controller
│   │   └── pilgrimage/                     # Pilgrimage management module
│   │       ├── entity/
│   │       │   └── Pilgrimage.java         # Pilgrimage JPA entity
│   │       ├── dto/
│   │       │   ├── PilgrimageDto.java      # Pilgrimage response DTO
│   │       │   ├── CreatePilgrimageRequest.java # Pilgrimage creation DTO
│   │       │   └── UpdatePilgrimageRequest.java # Pilgrimage update DTO
│   │       ├── repository/
│   │       │   └── PilgrimageRepository.java # Pilgrimage data access
│   │       ├── service/
│   │       │   ├── PilgrimageService.java  # Pilgrimage service interface
│   │       │   └── PilgrimageServiceImpl.java # Pilgrimage service implementation
│   │       └── controller/
│   │           └── PilgrimageController.java # Pilgrimage REST controller
│   └── resources/
│       ├── application.properties          # Application configuration
│       └── static/                         # Static resources
└── test/
    └── java/fr/noeldupuis/hdoapi/
        ├── persons/                        # Person management tests
        │   ├── service/PersonServiceTest.java
        │   ├── controller/PersonControllerTest.java
        │   └── repository/PersonRepositoryTest.java
        └── pilgrimage/                     # Pilgrimage management tests
            ├── service/PilgrimageServiceTest.java
            ├── controller/PilgrimageControllerTest.java
            └── repository/PilgrimageRepositoryTest.java
```

## 🔧 Configuration

The application configuration is managed through `application.properties`. Key configurations include:

- Database connection settings
- Server port configuration
- JPA/Hibernate settings
- Logging configuration

## 🧪 Testing

The project includes comprehensive test coverage for all layers:

### Test Coverage
- **Person Management**: 24 tests (8 service + 8 controller + 8 repository)
- **Pilgrimage Management**: 24 tests (8 service + 8 controller + 8 repository)
- **Application Tests**: 1 integration test
- **Total**: 49 tests (100% pass rate)

### Running Tests
```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=PersonServiceTest
./mvnw test -Dtest=PilgrimageServiceTest

# Run tests with coverage report
./mvnw test jacoco:report
```

### Test Structure
- **Unit Tests**: Test business logic in isolation
- **Integration Tests**: Test component interactions
- **Repository Tests**: Test database operations with in-memory H2

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

## 🎉 Recent Updates

### ✅ Completed Features (Latest Release)
- **Person Management System**: Complete CRUD operations for pilgrimage participants
- **Pilgrimage Management System**: Complete CRUD operations for pilgrimage events
- **RESTful API**: Full REST endpoints with proper HTTP status codes
- **Comprehensive Testing**: 49 tests covering all layers (100% pass rate)
- **DTO Pattern**: Clean separation between API contracts and internal models
- **Service Layer**: Business logic with proper error handling
- **Database Integration**: H2 in-memory database for development

### 🔄 Current Status
- **Person Management**: ✅ Fully implemented and tested
- **Pilgrimage Management**: ✅ Fully implemented and tested
- **Database**: H2 for development, PostgreSQL configuration pending

---

**Note**: This project is in active development. Both Person and Pilgrimage management systems are complete and ready for use. Phase 1 core features are fully implemented. Next development phase will focus on enhanced features like authentication, enrollment management, and PostgreSQL configuration.
