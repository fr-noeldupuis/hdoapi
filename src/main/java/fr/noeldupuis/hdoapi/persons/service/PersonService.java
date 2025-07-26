package fr.noeldupuis.hdoapi.persons.service;

import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.Optional;

public interface PersonService {
    
    Page<PersonDto> getAllPersons(Pageable pageable);
    
    Optional<PersonDto> getPersonById(Long id);
    
    PersonDto createPerson(CreatePersonRequest request);
    
    Optional<PersonDto> updatePerson(Long id, UpdatePersonRequest request);
    
    // JSON Merge Patch (RFC 7386) for partial updates
    PersonDto patchPerson(Long id, JsonNode patch);
    
    boolean deletePerson(Long id);
} 