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
  - Spring HATEOAS for hypermedia support
  - Lombok for reducing boilerplate code
  - JUnit 5 for testing
  - Mockito for mocking in tests
  - AssertJ for fluent assertions

## 🚀 Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6+
- Docker and Docker Compose (for containerized deployment)
- PostgreSQL (for production)

### Installation

#### Option 1: Local Development

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

#### Option 2: Docker Deployment (Recommended)

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd hdoapi
   ```

2. **Make the helper script executable**
   ```bash
   chmod +x docker-scripts.sh
   ```

3. **Start the application**

   **For Production (PostgreSQL):**
   ```bash
   ./docker-scripts.sh prod
   # or
   docker-compose up -d
   ```

   **For Development (H2):**
   ```bash
   ./docker-scripts.sh dev
   # or
   docker-compose -f docker-compose.dev.yml up -d
   ```

### Development Setup

For development, the application uses H2 in-memory database. The H2 console is available at:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: (leave empty)

### Docker Management

The project includes comprehensive Docker support with helper scripts:

```bash
# Build Docker image
./docker-scripts.sh build

# Start development environment (H2 database)
./docker-scripts.sh dev

# Start production environment (PostgreSQL)
./docker-scripts.sh prod

# Stop all containers
./docker-scripts.sh stop

# View logs
./docker-scripts.sh logs

# Clean up everything
./docker-scripts.sh clean

# Access container shell
./docker-scripts.sh shell

# Run tests in Docker
./docker-scripts.sh test

# Show help
./docker-scripts.sh help
```

**Docker Services:**
- **Production**: PostgreSQL database + HDO API application
- **Development**: H2 in-memory database + HDO API application
- **Ports**: 
  - API: `http://localhost:8080`
  - PostgreSQL: `localhost:5432`
  - H2 Console: `http://localhost:8080/h2-console`

## 📋 API Endpoints

The API currently provides the following endpoints with **HATEOAS support** and **pagination**:

### Person Management ✅ (Implemented)
- `GET /api/persons` - List all participants (with pagination & sorting)
- `GET /api/persons/{id}` - Get participant details
- `POST /api/persons` - Create new participant
- `PUT /api/persons/{id}` - Update participant
- `DELETE /api/persons/{id}` - Remove participant

**Pagination Parameters:**
- `page` (default: 0) - Page number (0-based)
- `size` (default: 10) - Number of items per page
- `sortBy` (default: "id") - Field to sort by
- `sortDir` (default: "asc") - Sort direction ("asc" or "desc")

**Request/Response Examples:**

**Get paginated persons:**
```bash
GET /api/persons?page=0&size=5&sortBy=firstName&sortDir=asc
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "firstName": "John",
      "lastName": "Doe",
      "birthDate": "1990-01-01",
      "_links": {
        "self": {"href": "/api/persons/1"},
        "collection": {"href": "/api/persons"}
      }
    }
  ],
  "pageMetadata": {
    "page": 0,
    "size": 5,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true,
    "hasNext": false,
    "hasPrevious": false
  },
  "_links": {
    "self": {"href": "/api/persons?page=0&size=5"}
  }
}
```

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
  "birthDate": "1990-01-01",
  "_links": {
    "self": {"href": "/api/persons/1"},
    "collection": {"href": "/api/persons"}
  }
}
```

### Pilgrimage Management ✅ (Implemented)
- `GET /api/pilgrimages` - List all pilgrimages (with pagination & sorting)
- `GET /api/pilgrimages/{id}` - Get pilgrimage details
- `POST /api/pilgrimages` - Create new pilgrimage
- `PUT /api/pilgrimages/{id}` - Update pilgrimage
- `DELETE /api/pilgrimages/{id}` - Remove pilgrimage

**Request/Response Examples:**

**Get paginated pilgrimages:**
```bash
GET /api/pilgrimages?page=0&size=10&sortBy=name&sortDir=asc
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "Lourdes 2025",
      "startDate": "2025-06-15",
      "endDate": "2025-06-22",
      "_links": {
        "self": {"href": "/api/pilgrimages/1"},
        "collection": {"href": "/api/pilgrimages"}
      }
    }
  ],
  "pageMetadata": {
    "page": 0,
    "size": 10,
    "totalElements": 1,
    "totalPages": 1,
    "first": true,
    "last": true,
    "hasNext": false,
    "hasPrevious": false
  },
  "_links": {
    "self": {"href": "/api/pilgrimages?page=0&size=10"}
  }
}
```

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
  "endDate": "2025-06-22",
  "_links": {
    "self": {"href": "/api/pilgrimages/1"},
    "collection": {"href": "/api/pilgrimages"}
  }
}
```

## 🗺 Roadmap

### Phase 1: Core Features ✅ (Completed)
- [x] Set up PostgreSQL database configuration
- [x] Implement Person entity and repository
- [x] Create Person REST controller
- [x] Basic CRUD operations for Person entity
- [x] Implement Pilgrimage entity and repository
- [x] Create Pilgrimage REST controller
- [x] Basic CRUD operations for Pilgrimage entity
- [x] Docker containerization setup
- [x] Multi-environment Docker configurations

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
hdoapi/
├── src/
│   ├── main/
│   │   ├── java/fr/noeldupuis/hdoapi/
│   │   │   ├── HdoapiApplication.java          # Main application class
│   │   │   ├── persons/                        # Person management module
│   │   │   │   ├── entity/
│   │   │   │   │   └── Person.java             # Person JPA entity
│   │   │   │   ├── dto/
│   │   │   │   │   ├── PersonDto.java          # Person response DTO
│   │   │   │   │   ├── CreatePersonRequest.java # Person creation DTO
│   │   │   │   │   └── UpdatePersonRequest.java # Person update DTO
│   │   │   │   ├── repository/
│   │   │   │   │   └── PersonRepository.java   # Person data access
│   │   │   │   ├── service/
│   │   │   │   │   ├── PersonService.java      # Person service interface
│   │   │   │   │   └── PersonServiceImpl.java  # Person service implementation
│   │   │   │   └── controller/
│   │   │   │       └── PersonController.java   # Person REST controller
│   │   │   └── pilgrimage/                     # Pilgrimage management module
│   │   │       ├── entity/
│   │   │       │   └── Pilgrimage.java         # Pilgrimage JPA entity
│   │   │       ├── dto/
│   │   │       │   ├── PilgrimageDto.java      # Pilgrimage response DTO
│   │   │       │   ├── CreatePilgrimageRequest.java # Pilgrimage creation DTO
│   │   │       │   └── UpdatePilgrimageRequest.java # Pilgrimage update DTO
│   │   │       ├── repository/
│   │   │       │   └── PilgrimageRepository.java # Pilgrimage data access
│   │   │       ├── service/
│   │   │       │   ├── PilgrimageService.java  # Pilgrimage service interface
│   │   │       │   └── PilgrimageServiceImpl.java # Pilgrimage service implementation
│   │   │       └── controller/
│   │   │           └── PilgrimageController.java # Pilgrimage REST controller
│   │   └── resources/
│   │       ├── application.properties          # Application configuration
│   │       └── static/                         # Static resources
│   └── test/
│       └── java/fr/noeldupuis/hdoapi/
│           ├── persons/                        # Person management tests
│           │   ├── service/PersonServiceTest.java
│           │   ├── controller/PersonControllerTest.java
│           │   └── repository/PersonRepositoryTest.java
│           └── pilgrimage/                     # Pilgrimage management tests
│               ├── service/PilgrimageServiceTest.java
│               ├── controller/PilgrimageControllerTest.java
│               └── repository/PilgrimageRepositoryTest.java
├── Dockerfile                               # Multi-stage Docker build
├── .dockerignore                            # Docker build exclusions
├── docker-compose.yml                       # Production environment (PostgreSQL)
├── docker-compose.dev.yml                   # Development environment (H2)
├── docker-scripts.sh                        # Docker management helper script
├── pom.xml                                  # Maven project configuration
├── LICENSE                                  # Apache 2.0 License
└── README.md                                # Project documentation
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
- **HATEOAS Support**: Hypermedia-driven API with self-discoverable links
- **Pagination & Sorting**: Efficient data retrieval with configurable page size and sorting
- **Comprehensive Testing**: 49 tests covering all layers (100% pass rate)
- **DTO Pattern**: Clean separation between API contracts and internal models
- **Service Layer**: Business logic with proper error handling
- **Database Integration**: H2 in-memory database for development
- **Docker Containerization**: Complete Docker setup with multi-stage builds
- **Multi-Environment Support**: Production (PostgreSQL) and Development (H2) configurations
- **Docker Management Scripts**: Comprehensive helper scripts for Docker operations

### 🔄 Current Status
- **Person Management**: ✅ Fully implemented and tested
- **Pilgrimage Management**: ✅ Fully implemented and tested
- **Database**: H2 for development, PostgreSQL for production (both containerized)
- **Docker Setup**: ✅ Production-ready with PostgreSQL and development with H2
- **Deployment**: Ready for containerized deployment

---

**Note**: This project is in active development. Both Person and Pilgrimage management systems are complete and ready for use. Phase 1 core features are fully implemented including Docker containerization. The application is now ready for both local development and production deployment using Docker. Next development phase will focus on enhanced features like authentication, enrollment management, and advanced business logic.
