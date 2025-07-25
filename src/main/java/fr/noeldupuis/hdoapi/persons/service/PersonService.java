package fr.noeldupuis.hdoapi.persons.service;

import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;

import java.util.List;
import java.util.Optional;

public interface PersonService {
    
    List<PersonDto> getAllPersons();
    
    Optional<PersonDto> getPersonById(Long id);
    
    PersonDto createPerson(CreatePersonRequest request);
    
    Optional<PersonDto> updatePerson(Long id, UpdatePersonRequest request);
    
    boolean deletePerson(Long id);
} 