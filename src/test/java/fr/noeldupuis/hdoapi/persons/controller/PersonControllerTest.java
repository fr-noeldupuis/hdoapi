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
import org.mockito.Mock;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
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

    @MockBean
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
        when(personService.getAllPersons()).thenReturn(persons);

        // When & Then
        mockMvc.perform(get("/api/persons"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));

        verify(personService).getAllPersons();
    }

    @Test
    void getPersonById_WhenPersonExists_ShouldReturnPerson() throws Exception {
        // Given
        when(personService.getPersonById(1L)).thenReturn(Optional.of(personDto1));

        // When & Then
        mockMvc.perform(get("/api/persons/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));

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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));

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
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Updated"))
                .andExpect(jsonPath("$.birthDate").value("1990-01-01"));

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