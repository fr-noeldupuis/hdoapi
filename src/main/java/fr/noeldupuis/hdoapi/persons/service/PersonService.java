package fr.noeldupuis.hdoapi.persons.service;

import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PartialUpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PatchPersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    
    Page<PersonDto> getAllPersons(Pageable pageable);
    
    Optional<PersonDto> getPersonById(Long id);
    
    PersonDto createPerson(CreatePersonRequest request);
    
    Optional<PersonDto> updatePerson(Long id, UpdatePersonRequest request);
    
    // PATCH methods for partial updates
    PersonDto partialUpdatePerson(Long id, PartialUpdatePersonRequest request);
    
    PersonDto patchPerson(Long id, List<PatchPersonRequest.Operation> operations);
    
    PersonDto mergePatchPerson(Long id, JsonNode patch);
    
    boolean deletePerson(Long id);
} 