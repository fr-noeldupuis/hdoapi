package fr.noeldupuis.hdoapi.persons.repository;

import fr.noeldupuis.hdoapi.persons.entity.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class PersonRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PersonRepository personRepository;

    private Person person1;
    private Person person2;

    @BeforeEach
    void setUp() {
        person1 = new Person(null, "John", "Doe", LocalDate.of(1990, 1, 1));
        person2 = new Person(null, "Jane", "Smith", LocalDate.of(1985, 5, 15));
    }

    @Test
    void save_ShouldPersistPerson() {
        // When
        Person savedPerson = personRepository.save(person1);

        // Then
        assertThat(savedPerson.getId()).isNotNull();
        assertThat(savedPerson.getFirstName()).isEqualTo("John");
        assertThat(savedPerson.getLastName()).isEqualTo("Doe");
        assertThat(savedPerson.getBirthDate()).isEqualTo(LocalDate.of(1990, 1, 1));
    }

    @Test
    void findById_WhenPersonExists_ShouldReturnPerson() {
        // Given
        Person savedPerson = entityManager.persistAndFlush(person1);

        // When
        Optional<Person> foundPerson = personRepository.findById(savedPerson.getId());

        // Then
        assertThat(foundPerson).isPresent();
        assertThat(foundPerson.get().getFirstName()).isEqualTo("John");
        assertThat(foundPerson.get().getLastName()).isEqualTo("Doe");
    }

    @Test
    void findById_WhenPersonDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<Person> foundPerson = personRepository.findById(999L);

        // Then
        assertThat(foundPerson).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllPersons() {
        // Given
        entityManager.persistAndFlush(person1);
        entityManager.persistAndFlush(person2);

        // When
        List<Person> persons = personRepository.findAll();

        // Then
        assertThat(persons).hasSize(2);
        assertThat(persons).extracting("firstName").containsExactlyInAnyOrder("John", "Jane");
        assertThat(persons).extracting("lastName").containsExactlyInAnyOrder("Doe", "Smith");
    }

    @Test
    void save_WhenUpdatingExistingPerson_ShouldUpdatePerson() {
        // Given
        Person savedPerson = entityManager.persistAndFlush(person1);
        savedPerson.setFirstName("Updated");
        savedPerson.setLastName("Name");

        // When
        Person updatedPerson = personRepository.save(savedPerson);

        // Then
        assertThat(updatedPerson.getId()).isEqualTo(savedPerson.getId());
        assertThat(updatedPerson.getFirstName()).isEqualTo("Updated");
        assertThat(updatedPerson.getLastName()).isEqualTo("Name");
    }

    @Test
    void deleteById_ShouldRemovePerson() {
        // Given
        Person savedPerson = entityManager.persistAndFlush(person1);

        // When
        personRepository.deleteById(savedPerson.getId());

        // Then
        Optional<Person> foundPerson = personRepository.findById(savedPerson.getId());
        assertThat(foundPerson).isEmpty();
    }

    @Test
    void existsById_WhenPersonExists_ShouldReturnTrue() {
        // Given
        Person savedPerson = entityManager.persistAndFlush(person1);

        // When
        boolean exists = personRepository.existsById(savedPerson.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_WhenPersonDoesNotExist_ShouldReturnFalse() {
        // When
        boolean exists = personRepository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }
} 