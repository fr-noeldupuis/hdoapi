package fr.noeldupuis.hdoapi.persons.service;

import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PersonServiceTest {

    @Mock
    private PersonRepository personRepository;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person person1;
    private Person person2;
    private PersonDto personDto1;
    private PersonDto personDto2;

    @BeforeEach
    void setUp() {
        person1 = new Person(1L, "John", "Doe", LocalDate.of(1990, 1, 1));
        person2 = new Person(2L, "Jane", "Smith", LocalDate.of(1985, 5, 15));
        
        personDto1 = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1));
        personDto2 = new PersonDto(2L, "Jane", "Smith", LocalDate.of(1985, 5, 15));
    }

    @Test
    void getAllPersons_ShouldReturnAllPersons() {
        // Given
        List<Person> persons = Arrays.asList(person1, person2);
        when(personRepository.findAll()).thenReturn(persons);

        // When
        List<PersonDto> result = personService.getAllPersons();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(personDto1, personDto2);
        verify(personRepository).findAll();
    }

    @Test
    void getPersonById_WhenPersonExists_ShouldReturnPerson() {
        // Given
        when(personRepository.findById(1L)).thenReturn(Optional.of(person1));

        // When
        Optional<PersonDto> result = personService.getPersonById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(personDto1);
        verify(personRepository).findById(1L);
    }

    @Test
    void getPersonById_WhenPersonDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<PersonDto> result = personService.getPersonById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(personRepository).findById(999L);
    }

    @Test
    void createPerson_ShouldCreateAndReturnPerson() {
        // Given
        CreatePersonRequest request = new CreatePersonRequest("John", "Doe", LocalDate.of(1990, 1, 1));
        when(personRepository.save(any(Person.class))).thenReturn(person1);

        // When
        PersonDto result = personService.createPerson(request);

        // Then
        assertThat(result).isEqualTo(personDto1);
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void updatePerson_WhenPersonExists_ShouldUpdateAndReturnPerson() {
        // Given
        UpdatePersonRequest request = new UpdatePersonRequest("John", "Updated", LocalDate.of(1990, 1, 1));
        Person updatedPerson = new Person(1L, "John", "Updated", LocalDate.of(1990, 1, 1));
        PersonDto expectedDto = new PersonDto(1L, "John", "Updated", LocalDate.of(1990, 1, 1));
        
        when(personRepository.findById(1L)).thenReturn(Optional.of(person1));
        when(personRepository.save(any(Person.class))).thenReturn(updatedPerson);

        // When
        Optional<PersonDto> result = personService.updatePerson(1L, request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDto);
        verify(personRepository).findById(1L);
        verify(personRepository).save(any(Person.class));
    }

    @Test
    void updatePerson_WhenPersonDoesNotExist_ShouldReturnEmpty() {
        // Given
        UpdatePersonRequest request = new UpdatePersonRequest("John", "Updated", LocalDate.of(1990, 1, 1));
        when(personRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<PersonDto> result = personService.updatePerson(999L, request);

        // Then
        assertThat(result).isEmpty();
        verify(personRepository).findById(999L);
        verify(personRepository, never()).save(any(Person.class));
    }

    @Test
    void deletePerson_WhenPersonExists_ShouldReturnTrue() {
        // Given
        when(personRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = personService.deletePerson(1L);

        // Then
        assertThat(result).isTrue();
        verify(personRepository).existsById(1L);
        verify(personRepository).deleteById(1L);
    }

    @Test
    void deletePerson_WhenPersonDoesNotExist_ShouldReturnFalse() {
        // Given
        when(personRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = personService.deletePerson(999L);

        // Then
        assertThat(result).isFalse();
        verify(personRepository).existsById(999L);
        verify(personRepository, never()).deleteById(any());
    }
} 