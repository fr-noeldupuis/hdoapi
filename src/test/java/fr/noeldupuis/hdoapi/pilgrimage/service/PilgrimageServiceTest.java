package fr.noeldupuis.hdoapi.pilgrimage.service;

import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
import fr.noeldupuis.hdoapi.pilgrimage.repository.PilgrimageRepository;
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
class PilgrimageServiceTest {

    @Mock
    private PilgrimageRepository pilgrimageRepository;

    @InjectMocks
    private PilgrimageServiceImpl pilgrimageService;

    private Pilgrimage pilgrimage1;
    private Pilgrimage pilgrimage2;
    private PilgrimageDto pilgrimageDto1;
    private PilgrimageDto pilgrimageDto2;

    @BeforeEach
    void setUp() {
        pilgrimage1 = new Pilgrimage(1L, "Lourdes 2025", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        pilgrimage2 = new Pilgrimage(2L, "Lourdes 2026", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 8));
        
        pilgrimageDto1 = new PilgrimageDto(1L, "Lourdes 2025", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        pilgrimageDto2 = new PilgrimageDto(2L, "Lourdes 2026", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 8));
    }

    @Test
    void getAllPilgrimages_ShouldReturnAllPilgrimages() {
        // Given
        List<Pilgrimage> pilgrimages = Arrays.asList(pilgrimage1, pilgrimage2);
        when(pilgrimageRepository.findAll()).thenReturn(pilgrimages);

        // When
        List<PilgrimageDto> result = pilgrimageService.getAllPilgrimages();

        // Then
        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(pilgrimageDto1, pilgrimageDto2);
        verify(pilgrimageRepository).findAll();
    }

    @Test
    void getPilgrimageById_WhenPilgrimageExists_ShouldReturnPilgrimage() {
        // Given
        when(pilgrimageRepository.findById(1L)).thenReturn(Optional.of(pilgrimage1));

        // When
        Optional<PilgrimageDto> result = pilgrimageService.getPilgrimageById(1L);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(pilgrimageDto1);
        verify(pilgrimageRepository).findById(1L);
    }

    @Test
    void getPilgrimageById_WhenPilgrimageDoesNotExist_ShouldReturnEmpty() {
        // Given
        when(pilgrimageRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<PilgrimageDto> result = pilgrimageService.getPilgrimageById(999L);

        // Then
        assertThat(result).isEmpty();
        verify(pilgrimageRepository).findById(999L);
    }

    @Test
    void createPilgrimage_ShouldCreateAndReturnPilgrimage() {
        // Given
        CreatePilgrimageRequest request = new CreatePilgrimageRequest("Lourdes 2025", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        when(pilgrimageRepository.save(any(Pilgrimage.class))).thenReturn(pilgrimage1);

        // When
        PilgrimageDto result = pilgrimageService.createPilgrimage(request);

        // Then
        assertThat(result).isEqualTo(pilgrimageDto1);
        verify(pilgrimageRepository).save(any(Pilgrimage.class));
    }

    @Test
    void updatePilgrimage_WhenPilgrimageExists_ShouldUpdateAndReturnPilgrimage() {
        // Given
        UpdatePilgrimageRequest request = new UpdatePilgrimageRequest("Lourdes 2025 Updated", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        Pilgrimage updatedPilgrimage = new Pilgrimage(1L, "Lourdes 2025 Updated", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        PilgrimageDto expectedDto = new PilgrimageDto(1L, "Lourdes 2025 Updated", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        
        when(pilgrimageRepository.findById(1L)).thenReturn(Optional.of(pilgrimage1));
        when(pilgrimageRepository.save(any(Pilgrimage.class))).thenReturn(updatedPilgrimage);

        // When
        Optional<PilgrimageDto> result = pilgrimageService.updatePilgrimage(1L, request);

        // Then
        assertThat(result).isPresent();
        assertThat(result.get()).isEqualTo(expectedDto);
        verify(pilgrimageRepository).findById(1L);
        verify(pilgrimageRepository).save(any(Pilgrimage.class));
    }

    @Test
    void updatePilgrimage_WhenPilgrimageDoesNotExist_ShouldReturnEmpty() {
        // Given
        UpdatePilgrimageRequest request = new UpdatePilgrimageRequest("Lourdes 2025 Updated", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        when(pilgrimageRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<PilgrimageDto> result = pilgrimageService.updatePilgrimage(999L, request);

        // Then
        assertThat(result).isEmpty();
        verify(pilgrimageRepository).findById(999L);
        verify(pilgrimageRepository, never()).save(any(Pilgrimage.class));
    }

    @Test
    void deletePilgrimage_WhenPilgrimageExists_ShouldReturnTrue() {
        // Given
        when(pilgrimageRepository.existsById(1L)).thenReturn(true);

        // When
        boolean result = pilgrimageService.deletePilgrimage(1L);

        // Then
        assertThat(result).isTrue();
        verify(pilgrimageRepository).existsById(1L);
        verify(pilgrimageRepository).deleteById(1L);
    }

    @Test
    void deletePilgrimage_WhenPilgrimageDoesNotExist_ShouldReturnFalse() {
        // Given
        when(pilgrimageRepository.existsById(999L)).thenReturn(false);

        // When
        boolean result = pilgrimageService.deletePilgrimage(999L);

        // Then
        assertThat(result).isFalse();
        verify(pilgrimageRepository).existsById(999L);
        verify(pilgrimageRepository, never()).deleteById(any());
    }
} 