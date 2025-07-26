package fr.noeldupuis.hdoapi.enrollment.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.enrollment.dto.CreateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.dto.EnrollmentDto;
import fr.noeldupuis.hdoapi.enrollment.dto.UpdateEnrollmentRequest;
import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import fr.noeldupuis.hdoapi.enrollment.service.EnrollmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(EnrollmentController.class)
class EnrollmentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EnrollmentService enrollmentService;

    @Autowired
    private ObjectMapper objectMapper;

    private EnrollmentDto enrollmentDto;
    private CreateEnrollmentRequest createRequest;
    private UpdateEnrollmentRequest updateRequest;

    @BeforeEach
    void setUp() {
        enrollmentDto = new EnrollmentDto();
        enrollmentDto.setId(1L);
        enrollmentDto.setPersonId(1L);
        enrollmentDto.setPersonName("John Doe");
        enrollmentDto.setPilgrimageId(1L);
        enrollmentDto.setPilgrimageName("Test Pilgrimage");
        enrollmentDto.setEnrollmentDate(LocalDateTime.now());
        enrollmentDto.setStatus(Enrollment.EnrollmentStatus.PENDING);
        enrollmentDto.setNotes("Test enrollment");

        createRequest = new CreateEnrollmentRequest();
        createRequest.setPersonId(1L);
        createRequest.setPilgrimageId(1L);
        createRequest.setNotes("Test enrollment");

        updateRequest = new UpdateEnrollmentRequest();
        updateRequest.setStatus(Enrollment.EnrollmentStatus.CONFIRMED);
        updateRequest.setNotes("Updated notes");
    }

    @Test
    void shouldCreateEnrollment() throws Exception {
        // Given
        when(enrollmentService.createEnrollment(any(CreateEnrollmentRequest.class))).thenReturn(enrollmentDto);

        // When & Then
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.enrollment.id").value(1))
                .andExpect(jsonPath("$.enrollment.personId").value(1))
                .andExpect(jsonPath("$.enrollment.pilgrimageId").value(1))
                .andExpect(jsonPath("$.enrollment.status").value("PENDING"));
    }

    @Test
    void shouldGetEnrollmentById() throws Exception {
        // Given
        when(enrollmentService.getEnrollmentById(1L)).thenReturn(enrollmentDto);

        // When & Then
        mockMvc.perform(get("/api/enrollments/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollment.id").value(1))
                .andExpect(jsonPath("$.enrollment.personName").value("John Doe"))
                .andExpect(jsonPath("$.enrollment.pilgrimageName").value("Test Pilgrimage"));
    }

    @Test
    void shouldGetAllEnrollments() throws Exception {
        // Given
        Page<EnrollmentDto> page = new PageImpl<>(List.of(enrollmentDto), PageRequest.of(0, 10), 1);
        PagedResponse<EnrollmentDto> pagedResponse = new PagedResponse<>(
                List.of(enrollmentDto), page, "/api/enrollments"
        );
        when(enrollmentService.getAllEnrollments(0, 10)).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/enrollments")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1));
    }

    @Test
    void shouldGetEnrollmentsByPersonId() throws Exception {
        // Given
        Page<EnrollmentDto> page = new PageImpl<>(List.of(enrollmentDto), PageRequest.of(0, 10), 1);
        PagedResponse<EnrollmentDto> pagedResponse = new PagedResponse<>(
                List.of(enrollmentDto), page, "/api/enrollments/person/1"
        );
        when(enrollmentService.getEnrollmentsByPersonId(1L, 0, 10)).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/enrollments/person/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].personId").value(1));
    }

    @Test
    void shouldGetEnrollmentsByPilgrimageId() throws Exception {
        // Given
        Page<EnrollmentDto> page = new PageImpl<>(List.of(enrollmentDto), PageRequest.of(0, 10), 1);
        PagedResponse<EnrollmentDto> pagedResponse = new PagedResponse<>(
                List.of(enrollmentDto), page, "/api/enrollments/pilgrimage/1"
        );
        when(enrollmentService.getEnrollmentsByPilgrimageId(1L, 0, 10)).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/enrollments/pilgrimage/1")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].pilgrimageId").value(1));
    }

    @Test
    void shouldGetEnrollmentsByStatus() throws Exception {
        // Given
        Page<EnrollmentDto> page = new PageImpl<>(List.of(enrollmentDto), PageRequest.of(0, 10), 1);
        PagedResponse<EnrollmentDto> pagedResponse = new PagedResponse<>(
                List.of(enrollmentDto), page, "/api/enrollments/status/PENDING"
        );
        when(enrollmentService.getEnrollmentsByStatus(Enrollment.EnrollmentStatus.PENDING, 0, 10)).thenReturn(pagedResponse);

        // When & Then
        mockMvc.perform(get("/api/enrollments/status/PENDING")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].status").value("PENDING"));
    }

    @Test
    void shouldUpdateEnrollment() throws Exception {
        // Given
        when(enrollmentService.updateEnrollment(eq(1L), any(UpdateEnrollmentRequest.class))).thenReturn(enrollmentDto);

        // When & Then
        mockMvc.perform(put("/api/enrollments/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.enrollment.id").value(1));
    }

    @Test
    void shouldDeleteEnrollment() throws Exception {
        // Given
        // No need to mock anything for void method

        // When & Then
        mockMvc.perform(delete("/api/enrollments/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldCheckEnrollmentExists() throws Exception {
        // Given
        when(enrollmentService.existsByPersonIdAndPilgrimageId(1L, 1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(get("/api/enrollments/check")
                        .param("personId", "1")
                        .param("pilgrimageId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("true"));
    }

    @Test
    void shouldCountEnrollmentsByPilgrimageIdAndStatus() throws Exception {
        // Given
        when(enrollmentService.countByPilgrimageIdAndStatus(1L, Enrollment.EnrollmentStatus.PENDING)).thenReturn(5L);

        // When & Then
        mockMvc.perform(get("/api/enrollments/count")
                        .param("pilgrimageId", "1")
                        .param("status", "PENDING"))
                .andExpect(status().isOk())
                .andExpect(content().string("5"));
    }

    @Test
    void shouldReturnBadRequestForInvalidCreateRequest() throws Exception {
        // Given
        CreateEnrollmentRequest invalidRequest = new CreateEnrollmentRequest();
        // Missing required fields

        // When & Then
        mockMvc.perform(post("/api/enrollments")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidRequest)))
                .andExpect(status().isBadRequest());
    }
} 