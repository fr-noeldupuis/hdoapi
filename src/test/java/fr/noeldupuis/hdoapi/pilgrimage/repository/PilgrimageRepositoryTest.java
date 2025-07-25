package fr.noeldupuis.hdoapi.pilgrimage.repository;

import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
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
class PilgrimageRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PilgrimageRepository pilgrimageRepository;

    private Pilgrimage pilgrimage1;
    private Pilgrimage pilgrimage2;

    @BeforeEach
    void setUp() {
        pilgrimage1 = new Pilgrimage(null, "Lourdes 2025", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        pilgrimage2 = new Pilgrimage(null, "Lourdes 2026", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 8));
    }

    @Test
    void save_ShouldPersistPilgrimage() {
        // When
        Pilgrimage savedPilgrimage = pilgrimageRepository.save(pilgrimage1);

        // Then
        assertThat(savedPilgrimage.getId()).isNotNull();
        assertThat(savedPilgrimage.getName()).isEqualTo("Lourdes 2025");
        assertThat(savedPilgrimage.getStartDate()).isEqualTo(LocalDate.of(2025, 6, 15));
        assertThat(savedPilgrimage.getEndDate()).isEqualTo(LocalDate.of(2025, 6, 22));
    }

    @Test
    void findById_WhenPilgrimageExists_ShouldReturnPilgrimage() {
        // Given
        Pilgrimage savedPilgrimage = entityManager.persistAndFlush(pilgrimage1);

        // When
        Optional<Pilgrimage> foundPilgrimage = pilgrimageRepository.findById(savedPilgrimage.getId());

        // Then
        assertThat(foundPilgrimage).isPresent();
        assertThat(foundPilgrimage.get().getName()).isEqualTo("Lourdes 2025");
        assertThat(foundPilgrimage.get().getStartDate()).isEqualTo(LocalDate.of(2025, 6, 15));
    }

    @Test
    void findById_WhenPilgrimageDoesNotExist_ShouldReturnEmpty() {
        // When
        Optional<Pilgrimage> foundPilgrimage = pilgrimageRepository.findById(999L);

        // Then
        assertThat(foundPilgrimage).isEmpty();
    }

    @Test
    void findAll_ShouldReturnAllPilgrimages() {
        // Given
        entityManager.persistAndFlush(pilgrimage1);
        entityManager.persistAndFlush(pilgrimage2);

        // When
        List<Pilgrimage> pilgrimages = pilgrimageRepository.findAll();

        // Then
        assertThat(pilgrimages).hasSize(2);
        assertThat(pilgrimages).extracting("name").containsExactlyInAnyOrder("Lourdes 2025", "Lourdes 2026");
        assertThat(pilgrimages).extracting("startDate").containsExactlyInAnyOrder(
                LocalDate.of(2025, 6, 15), LocalDate.of(2026, 7, 1));
    }

    @Test
    void save_WhenUpdatingExistingPilgrimage_ShouldUpdatePilgrimage() {
        // Given
        Pilgrimage savedPilgrimage = entityManager.persistAndFlush(pilgrimage1);
        savedPilgrimage.setName("Lourdes 2025 Updated");
        savedPilgrimage.setEndDate(LocalDate.of(2025, 6, 25));

        // When
        Pilgrimage updatedPilgrimage = pilgrimageRepository.save(savedPilgrimage);

        // Then
        assertThat(updatedPilgrimage.getId()).isEqualTo(savedPilgrimage.getId());
        assertThat(updatedPilgrimage.getName()).isEqualTo("Lourdes 2025 Updated");
        assertThat(updatedPilgrimage.getEndDate()).isEqualTo(LocalDate.of(2025, 6, 25));
    }

    @Test
    void deleteById_ShouldRemovePilgrimage() {
        // Given
        Pilgrimage savedPilgrimage = entityManager.persistAndFlush(pilgrimage1);

        // When
        pilgrimageRepository.deleteById(savedPilgrimage.getId());

        // Then
        Optional<Pilgrimage> foundPilgrimage = pilgrimageRepository.findById(savedPilgrimage.getId());
        assertThat(foundPilgrimage).isEmpty();
    }

    @Test
    void existsById_WhenPilgrimageExists_ShouldReturnTrue() {
        // Given
        Pilgrimage savedPilgrimage = entityManager.persistAndFlush(pilgrimage1);

        // When
        boolean exists = pilgrimageRepository.existsById(savedPilgrimage.getId());

        // Then
        assertThat(exists).isTrue();
    }

    @Test
    void existsById_WhenPilgrimageDoesNotExist_ShouldReturnFalse() {
        // When
        boolean exists = pilgrimageRepository.existsById(999L);

        // Then
        assertThat(exists).isFalse();
    }
} 