#!/bin/bash

# HDO API Docker Management Script

set -e

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Function to print colored output
print_status() {
    echo -e "${BLUE}[INFO]${NC} $1"
}

print_success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# Function to show usage
show_usage() {
    echo "HDO API Docker Management Script"
    echo ""
    echo "Usage: $0 [COMMAND]"
    echo ""
    echo "Commands:"
    echo "  build           Build the Docker image"
    echo "  dev             Start development environment (H2 database)"
    echo "  prod            Start production environment (PostgreSQL)"
    echo "  stop            Stop all containers"
    echo "  clean           Remove all containers and images"
    echo "  logs            Show application logs"
    echo "  shell           Open shell in running container"
    echo "  test            Run tests in container"
    echo "  help            Show this help message"
    echo ""
    echo "Examples:"
    echo "  $0 build        # Build the Docker image"
    echo "  $0 dev          # Start development environment"
    echo "  $0 prod         # Start production environment"
    echo "  $0 stop         # Stop all containers"
}

# Function to build Docker image
build_image() {
    print_status "Building Docker image..."
    docker build -t hdoapi:latest .
    print_success "Docker image built successfully!"
}

# Function to start development environment
start_dev() {
    print_status "Starting development environment with H2 database..."
    docker-compose -f docker-compose.dev.yml up -d
    print_success "Development environment started!"
    print_status "Application available at: http://localhost:8080"
    print_status "H2 Console available at: http://localhost:8080/h2-console"
}

# Function to start production environment
start_prod() {
    print_status "Starting production environment with PostgreSQL..."
    docker-compose up -d
    print_success "Production environment started!"
    print_status "Application available at: http://localhost:8080"
    print_status "PostgreSQL available at: localhost:5432"
}

# Function to stop all containers
stop_containers() {
    print_status "Stopping all containers..."
    docker-compose down
    docker-compose -f docker-compose.dev.yml down
    print_success "All containers stopped!"
}

# Function to clean up
clean_up() {
    print_warning "This will remove all containers and images. Are you sure? (y/N)"
    read -r response
    if [[ "$response" =~ ^([yY][eE][sS]|[yY])$ ]]; then
        print_status "Cleaning up Docker resources..."
        docker-compose down -v --rmi all
        docker-compose -f docker-compose.dev.yml down -v --rmi all
        docker system prune -f
        print_success "Cleanup completed!"
    else
        print_status "Cleanup cancelled."
    fi
}

# Function to show logs
show_logs() {
    print_status "Showing application logs..."
    docker-compose logs -f hdoapi || docker-compose -f docker-compose.dev.yml logs -f hdoapi-dev
}

# Function to open shell in container
open_shell() {
    print_status "Opening shell in container..."
    docker-compose exec hdoapi sh || docker-compose -f docker-compose.dev.yml exec hdoapi-dev sh
}

# Function to run tests
run_tests() {
    print_status "Running tests in container..."
    docker run --rm hdoapi:latest mvn test
}

# Main script logic
case "${1:-help}" in
    build)
        build_image
        ;;
    dev)
        start_dev
        ;;
    prod)
        start_prod
        ;;
    stop)
        stop_containers
        ;;
    clean)
        clean_up
        ;;
    logs)
        show_logs
        ;;
    shell)
        open_shell
        ;;
    test)
        run_tests
        ;;
    help|--help|-h)
        show_usage
        ;;
    *)
        print_error "Unknown command: $1"
        echo ""
        show_usage
        exit 1
        ;;
esac 