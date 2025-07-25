package fr.noeldupuis.hdoapi.enrollment.service;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.enrollment.dto.CreateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.dto.EnrollmentDto;
import fr.noeldupuis.hdoapi.enrollment.dto.UpdateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import fr.noeldupuis.hdoapi.enrollment.repository.EnrollmentRepository;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
import fr.noeldupuis.hdoapi.pilgrimage.repository.PilgrimageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @Mock
    private PersonRepository personRepository;

    @Mock
    private PilgrimageRepository pilgrimageRepository;

    @InjectMocks
    private EnrollmentServiceImpl enrollmentService;

    private Person person;
    private Pilgrimage pilgrimage;
    private Enrollment enrollment;
    private CreateEnrollmentRequest createRequest;
    private UpdateEnrollmentRequest updateRequest;

    @BeforeEach
    void setUp() {
        // Setup test data
        person = new Person();
        person.setId(1L);
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));

        pilgrimage = new Pilgrimage();
        pilgrimage.setId(1L);
        pilgrimage.setName("Test Pilgrimage");
        pilgrimage.setStartDate(LocalDate.of(2024, 6, 1));
        pilgrimage.setEndDate(LocalDate.of(2024, 6, 7));

        enrollment = new Enrollment();
        enrollment.setId(1L);
        enrollment.setPerson(person);
        enrollment.setPilgrimage(pilgrimage);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(Enrollment.EnrollmentStatus.PENDING);
        enrollment.setNotes("Test enrollment");

        createRequest = new CreateEnrollmentRequest();
        createRequest.setPersonId(1L);
        createRequest.setPilgrimageId(1L);
        createRequest.setNotes("Test enrollment");

        updateRequest = new UpdateEnrollmentRequest();
        updateRequest.setStatus(Enrollment.EnrollmentStatus.CONFIRMED);
        updateRequest.setNotes("Updated notes");
    }

    @Test
    void shouldCreateEnrollment() {
        // Given
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(pilgrimageRepository.findById(1L)).thenReturn(Optional.of(pilgrimage));
        when(enrollmentRepository.existsByPersonIdAndPilgrimageId(1L, 1L)).thenReturn(false);
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        // When
        EnrollmentDto result = enrollmentService.createEnrollment(createRequest);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getPersonId()).isEqualTo(1L);
        assertThat(result.getPilgrimageId()).isEqualTo(1L);
        assertThat(result.getStatus()).isEqualTo(Enrollment.EnrollmentStatus.PENDING);
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void shouldThrowExceptionWhenPersonNotFound() {
        // Given
        when(personRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enrollmentService.createEnrollment(createRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Person not found with id: 1");
    }

    @Test
    void shouldThrowExceptionWhenPilgrimageNotFound() {
        // Given
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(pilgrimageRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enrollmentService.createEnrollment(createRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Pilgrimage not found with id: 1");
    }

    @Test
    void shouldThrowExceptionWhenEnrollmentAlreadyExists() {
        // Given
        when(personRepository.findById(1L)).thenReturn(Optional.of(person));
        when(pilgrimageRepository.findById(1L)).thenReturn(Optional.of(pilgrimage));
        when(enrollmentRepository.existsByPersonIdAndPilgrimageId(1L, 1L)).thenReturn(true);

        // When & Then
        assertThatThrownBy(() -> enrollmentService.createEnrollment(createRequest))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Enrollment already exists for this person and pilgrimage");
    }

    @Test
    void shouldGetEnrollmentById() {
        // Given
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        // When
        EnrollmentDto result = enrollmentService.getEnrollmentById(1L);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getPersonName()).isEqualTo("John Doe");
        assertThat(result.getPilgrimageName()).isEqualTo("Test Pilgrimage");
    }

    @Test
    void shouldThrowExceptionWhenEnrollmentNotFound() {
        // Given
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> enrollmentService.getEnrollmentById(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Enrollment not found with id: 1");
    }

    @Test
    void shouldGetAllEnrollments() {
        // Given
        Page<Enrollment> page = new PageImpl<>(List.of(enrollment));
        when(enrollmentRepository.findAll(any(Pageable.class))).thenReturn(page);

        // When
        PagedResponse<EnrollmentDto> result = enrollmentService.getAllEnrollments(0, 10);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getId()).isEqualTo(1L);
    }

    @Test
    void shouldGetEnrollmentsByPersonId() {
        // Given
        Page<Enrollment> page = new PageImpl<>(List.of(enrollment));
        when(enrollmentRepository.findByPersonId(eq(1L), any(Pageable.class))).thenReturn(page);

        // When
        PagedResponse<EnrollmentDto> result = enrollmentService.getEnrollmentsByPersonId(1L, 0, 10);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPersonId()).isEqualTo(1L);
    }

    @Test
    void shouldGetEnrollmentsByPilgrimageId() {
        // Given
        Page<Enrollment> page = new PageImpl<>(List.of(enrollment));
        when(enrollmentRepository.findByPilgrimageId(eq(1L), any(Pageable.class))).thenReturn(page);

        // When
        PagedResponse<EnrollmentDto> result = enrollmentService.getEnrollmentsByPilgrimageId(1L, 0, 10);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPilgrimageId()).isEqualTo(1L);
    }

    @Test
    void shouldGetEnrollmentsByStatus() {
        // Given
        Page<Enrollment> page = new PageImpl<>(List.of(enrollment));
        when(enrollmentRepository.findByStatus(eq(Enrollment.EnrollmentStatus.PENDING), any(Pageable.class))).thenReturn(page);

        // When
        PagedResponse<EnrollmentDto> result = enrollmentService.getEnrollmentsByStatus(Enrollment.EnrollmentStatus.PENDING, 0, 10);

        // Then
        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(Enrollment.EnrollmentStatus.PENDING);
    }

    @Test
    void shouldUpdateEnrollment() {
        // Given
        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));
        when(enrollmentRepository.save(any(Enrollment.class))).thenReturn(enrollment);

        // When
        EnrollmentDto result = enrollmentService.updateEnrollment(1L, updateRequest);

        // Then
        assertThat(result).isNotNull();
        verify(enrollmentRepository).save(any(Enrollment.class));
    }

    @Test
    void shouldDeleteEnrollment() {
        // Given
        when(enrollmentRepository.existsById(1L)).thenReturn(true);

        // When
        enrollmentService.deleteEnrollment(1L);

        // Then
        verify(enrollmentRepository).deleteById(1L);
    }

    @Test
    void shouldThrowExceptionWhenDeletingNonExistentEnrollment() {
        // Given
        when(enrollmentRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> enrollmentService.deleteEnrollment(1L))
                .isInstanceOf(RuntimeException.class)
                .hasMessage("Enrollment not found with id: 1");
    }

    @Test
    void shouldCheckExistsByPersonIdAndPilgrimageId() {
        // Given
        when(enrollmentRepository.existsByPersonIdAndPilgrimageId(1L, 1L)).thenReturn(true);

        // When
        boolean result = enrollmentService.existsByPersonIdAndPilgrimageId(1L, 1L);

        // Then
        assertThat(result).isTrue();
    }

    @Test
    void shouldCountByPilgrimageIdAndStatus() {
        // Given
        when(enrollmentRepository.countByPilgrimageIdAndStatus(1L, Enrollment.EnrollmentStatus.PENDING)).thenReturn(5L);

        // When
        long result = enrollmentService.countByPilgrimageIdAndStatus(1L, Enrollment.EnrollmentStatus.PENDING);

        // Then
        assertThat(result).isEqualTo(5L);
    }
} 