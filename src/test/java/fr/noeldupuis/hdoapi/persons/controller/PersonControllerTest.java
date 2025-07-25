package fr.noeldupuis.hdoapi.persons.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

@WebMvcTest(PersonController.class)
class PersonControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PersonService personService;

    @Autowired
    private ObjectMapper objectMapper;

    private PersonDto personDto1;
    private PersonDto personDto2;
    private CreatePersonRequest createRequest;
    private UpdatePersonRequest updateRequest;

    @BeforeEach
    void setUp() {
        personDto1 = new PersonDto(1L, "John", "Doe", LocalDate.of(1990, 1, 1));
        personDto2 = new PersonDto(2L, "Jane", "Smith", LocalDate.of(1985, 5, 15));
        
        createRequest = new CreatePersonRequest("John", "Doe", LocalDate.of(1990, 1, 1));
        updateRequest = new UpdatePersonRequest("John", "Updated", LocalDate.of(1990, 1, 1));
    }

    @Test
    void getAllPersons_ShouldReturnAllPersons() throws Exception {
        // Given
        List<PersonDto> persons = Arrays.asList(personDto1, personDto2);
        Pageable pageable = PageRequest.of(0, 10);
        Page<PersonDto> personPage = new PageImpl<>(persons, pageable, persons.size());
        when(personService.getAllPersons(any(Pageable.class))).thenReturn(personPage);

        // When & Then
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[0].id").value(1))
                .andExpect(jsonPath("$.content[0].firstName").value("John"))
                .andExpect(jsonPath("$.content[0].lastName").value("Doe"))
                .andExpect(jsonPath("$.content[1].id").value(2))
                .andExpect(jsonPath("$.content[1].firstName").value("Jane"))
                .andExpect(jsonPath("$.content[1].lastName").value("Smith"))
                .andExpect(jsonPath("$.pageMetadata.page").value(0))
                .andExpect(jsonPath("$.pageMetadata.size").value(10))
                .andExpect(jsonPath("$.pageMetadata.totalElements").value(2));

        verify(personService).getAllPersons(any(Pageable.class));
    }

    @Test
    void getPersonById_WhenPersonExists_ShouldReturnPerson() throws Exception {
        // Given
        when(personService.getPersonById(1L)).thenReturn(Optional.of(personDto1));

        // When & Then
        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$._links.self.href").value("/api/persons/1"))
                .andExpect(jsonPath("$._links.collection.href").value("/api/persons"));

        verify(personService).getPersonById(1L);
    }

    @Test
    void getPersonById_WhenPersonDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(personService.getPersonById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/persons/999"))
                .andExpect(status().isNotFound());

        verify(personService).getPersonById(999L);
    }

    @Test
    void createPerson_ShouldCreateAndReturnPerson() throws Exception {
        // Given
        when(personService.createPerson(any(CreatePersonRequest.class))).thenReturn(personDto1);

        // When & Then
        mockMvc.perform(post("/api/persons")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$._links.self.href").value("/api/persons/1"))
                .andExpect(jsonPath("$._links.collection.href").value("/api/persons"));

        verify(personService).createPerson(any(CreatePersonRequest.class));
    }

    @Test
    void updatePerson_WhenPersonExists_ShouldUpdateAndReturnPerson() throws Exception {
        // Given
        PersonDto updatedPerson = new PersonDto(1L, "John", "Updated", LocalDate.of(1990, 1, 1));
        when(personService.updatePerson(eq(1L), any(UpdatePersonRequest.class)))
                .thenReturn(Optional.of(updatedPerson));

        // When & Then
        mockMvc.perform(put("/api/persons/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/hal+json"))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Updated"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"))
                .andExpect(jsonPath("$._links.self.href").value("/api/persons/1"))
                .andExpect(jsonPath("$._links.collection.href").value("/api/persons"));

        verify(personService).updatePerson(eq(1L), any(UpdatePersonRequest.class));
    }

    @Test
    void updatePerson_WhenPersonDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(personService.updatePerson(eq(999L), any(UpdatePersonRequest.class)))
                .thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(put("/api/persons/999")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isNotFound());

        verify(personService).updatePerson(eq(999L), any(UpdatePersonRequest.class));
    }

    @Test
    void deletePerson_WhenPersonExists_ShouldReturnNoContent() throws Exception {
        // Given
        when(personService.deletePerson(1L)).thenReturn(true);

        // When & Then
        mockMvc.perform(delete("/api/persons/1"))
                .andExpect(status().isNoContent());

        verify(personService).deletePerson(1L);
    }

    @Test
    void deletePerson_WhenPersonDoesNotExist_ShouldReturnNotFound() throws Exception {
        // Given
        when(personService.deletePerson(999L)).thenReturn(false);

        // When & Then
        mockMvc.perform(delete("/api/persons/999"))
                .andExpect(status().isNotFound());

        verify(personService).deletePerson(999L);
    }
} 