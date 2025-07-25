package fr.noeldupuis.hdoapi.enrollment.repository;

import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
import fr.noeldupuis.hdoapi.pilgrimage.repository.PilgrimageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class EnrollmentRepositoryTest {

    @Autowired
    private EnrollmentRepository enrollmentRepository;

    @Autowired
    private PersonRepository personRepository;

    @Autowired
    private PilgrimageRepository pilgrimageRepository;

    private Person person;
    private Pilgrimage pilgrimage;
    private Enrollment enrollment;

    @BeforeEach
    void setUp() {
        // Create test person
        person = new Person();
        person.setFirstName("John");
        person.setLastName("Doe");
        person.setBirthDate(LocalDate.of(1990, 1, 1));
        person = personRepository.save(person);

        // Create test pilgrimage
        pilgrimage = new Pilgrimage();
        pilgrimage.setName("Test Pilgrimage");
        pilgrimage.setStartDate(LocalDate.of(2024, 6, 1));
        pilgrimage.setEndDate(LocalDate.of(2024, 6, 7));
        pilgrimage = pilgrimageRepository.save(pilgrimage);

        // Create test enrollment
        enrollment = new Enrollment();
        enrollment.setPerson(person);
        enrollment.setPilgrimage(pilgrimage);
        enrollment.setEnrollmentDate(LocalDateTime.now());
        enrollment.setStatus(Enrollment.EnrollmentStatus.PENDING);
        enrollment.setNotes("Test enrollment");
        enrollment = enrollmentRepository.save(enrollment);
    }

    @Test
    void shouldFindByPersonId() {
        // When
        Page<Enrollment> result = enrollmentRepository.findByPersonId(person.getId(), PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPerson().getId()).isEqualTo(person.getId());
    }

    @Test
    void shouldFindByPilgrimageId() {
        // When
        Page<Enrollment> result = enrollmentRepository.findByPilgrimageId(pilgrimage.getId(), PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getPilgrimage().getId()).isEqualTo(pilgrimage.getId());
    }

    @Test
    void shouldFindByPersonIdAndPilgrimageId() {
        // When
        Optional<Enrollment> result = enrollmentRepository.findByPersonIdAndPilgrimageId(person.getId(), pilgrimage.getId());

        // Then
        assertThat(result).isPresent();
        assertThat(result.get().getPerson().getId()).isEqualTo(person.getId());
        assertThat(result.get().getPilgrimage().getId()).isEqualTo(pilgrimage.getId());
    }

    @Test
    void shouldFindByStatus() {
        // When
        Page<Enrollment> result = enrollmentRepository.findByStatus(Enrollment.EnrollmentStatus.PENDING, PageRequest.of(0, 10));

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getStatus()).isEqualTo(Enrollment.EnrollmentStatus.PENDING);
    }

    @Test
    void shouldCountByPilgrimageIdAndStatus() {
        // When
        long count = enrollmentRepository.countByPilgrimageIdAndStatus(pilgrimage.getId(), Enrollment.EnrollmentStatus.PENDING);

        // Then
        assertThat(count).isEqualTo(1);
    }

    @Test
    void shouldCheckExistsByPersonIdAndPilgrimageId() {
        // When
        boolean exists = enrollmentRepository.existsByPersonIdAndPilgrimageId(person.getId(), pilgrimage.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void shouldReturnFalseWhenEnrollmentDoesNotExist() {
        // When
        boolean exists = enrollmentRepository.existsByPersonIdAndPilgrimageId(999L, 999L);

        // Then
        assertThat(exists).isFalse();
    }
} 