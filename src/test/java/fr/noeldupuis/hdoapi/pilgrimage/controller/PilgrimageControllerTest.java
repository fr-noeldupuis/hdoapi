package fr.noeldupuis.hdoapi.pilgrimage.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.service.PilgrimageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PilgrimageController.class)
class PilgrimageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PilgrimageService pilgrimageService;

    @Autowired
    private ObjectMapper objectMapper;

    private PilgrimageDto pilgrimageDto1;
    private PilgrimageDto pilgrimageDto2;
    private CreatePilgrimageRequest createRequest;
    private UpdatePilgrimageRequest updateRequest;

    @BeforeEach
    void setUp() {
        pilgrimageDto1 = new PilgrimageDto(1L, "Lourdes 2025", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        pilgrimageDto2 = new PilgrimageDto(2L, "Lourdes 2026", LocalDate.of(2026, 7, 1), LocalDate.of(2026, 7, 8));
        
        createRequest = new CreatePilgrimageRequest("Lourdes 2025", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        updateRequest = new UpdatePilgrimageRequest("Lourdes 2025 Updated", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
    }

    @Test
    void getAllPilgrimages_ShouldReturnAllPilgrimages() throws Exception {
        // Given
        List<PilgrimageDto> pilgrimages = Arrays.asList(pilgrimageDto1, pilgrimageDto2);
        when(pilgrimageService.getAllPilgrimages()).thenReturn(pilgrimages);

        // When & Then
        mockMvc.perform(get("/api/pilgrimages"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Lourdes 2025"))
                .andExpect(jsonPath("$[0].startDate").value("2025-06-15"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Lourdes 2026"))
                .andExpect(jsonPath("$[1].startDate").value("2026-07-01"));

        verify(pilgrimageService).getAllPilgrimages();
    }

    @Test
    void getPilgrimageById_WhenPilgrimageExists_ShouldReturnPilgrimage() throws Exception {
        // Given
        when(pilgrimageService.getPilgrimageById(1L)).thenReturn(Optional.of(pilgrimageDto1));

        // When & Then
        mockMvc.perform(get("/api/pilgrimages/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lourdes 2025"))
                .andExpect(jsonPath("$.startDate").value("2025-06-15"))
                .andExpect(jsonPath("$.endDate").value("2025-06-22"));

        verify(pilgrimageService).getPilgrimageById(1L);
    }

    @Test
    void getPilgrimageById_WhenPilgrimageDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(pilgrimageService.getPilgrimageById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/pilgrimages/999"))
                .andExpect(status().isNotFound());

        verify(pilgrimageService).getPilgrimageById(999L);
    }

    @Test
    void createPilgrimage_ShouldCreateAndReturnPilgrimage() throws Exception {
        // Given
        when(pilgrimageService.createPilgrimage(any(CreatePilgrimageRequest.class))).thenReturn(pilgrimageDto1);

        // When & Then
        mockMvc.perform(post("/api/pilgrimages")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lourdes 2025"))
                .andExpect(jsonPath("$.startDate").value("2025-06-15"))
                .andExpect(jsonPath("$.endDate").value("2025-06-22"));

        verify(pilgrimageService).createPilgrimage(any(CreatePilgrimageRequest.class));
    }

    @Test
    void updatePilgrimage_WhenPilgrimageExists_ShouldUpdateAndReturnPilgrimage() throws Exception {
        // Given
        PilgrimageDto updatedPilgrimage = new PilgrimageDto(1L, "Lourdes 2025 Updated", LocalDate.of(2025, 6, 15), LocalDate.of(2025, 6, 22));
        when(pilgrimageService.updatePilgrimage(eq(1L), any(UpdatePilgrimageRequest.class)))
                .thenReturn(Optional.of(updatedPilgrimage));

        // When & Then
        mockMvc.perform(put("/api/pilgrimages/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Lourdes 2025 Updated"))
                .andExpect(jsonPath("$.startDate").value("2025-06-15"))
                .andExpect(jsonPath("$.endDate").value("2025-06-22"));

        verify(pilgrimageService).updatePilgrimage(eq(1L), any(UpdatePilgrimageRequest.class));
    }

    @Test
    void updatePilgrimage_WhenPilgrimageDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(pilgrimageService.updatePilgrimage(eq(999L), any(UpdatePilgrimageRequest.class)))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/pilgrimages/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(pilgrimageService).updatePilgrimage(eq(999L), any(UpdatePilgrimageRequest.class));
    }

    @Test
    void deletePilgrimage_WhenPilgrimageExists_ShouldReturnNoContent() throws Exception {
        // Given
        when(pilgrimageService.deletePilgrimage(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/pilgrimages/1"))
                .andExpect(status().isNoContent());

        verify(pilgrimageService).deletePilgrimage(1L);
    }

    @Test
    void deletePilgrimage_WhenPilgrimageDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(pilgrimageService.deletePilgrimage(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/pilgrimages/999"))
                .andExpect(status().isNotFound());

        verify(pilgrimageService).deletePilgrimage(999L);
    }
} 