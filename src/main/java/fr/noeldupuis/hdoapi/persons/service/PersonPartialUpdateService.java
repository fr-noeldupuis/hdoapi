package fr.noeldupuis.hdoapi.persons.service;

import fr.noeldupuis.hdoapi.persons.dto.PartialUpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonPartialUpdateService {
    
    private final PersonRepository personRepository;
    
    @Transactional
    public Person partialUpdatePerson(Long id, PartialUpdatePersonRequest request) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        
        // Only update fields that are provided (non-null)
        if (request.getFirstName() != null) {
            person.setFirstName(request.getFirstName());
        }
        
        if (request.getLastName() != null) {
            person.setLastName(request.getLastName());
        }
        
        if (request.getBirthDate() != null) {
            person.setBirthDate(request.getBirthDate());
        }
        
        return personRepository.save(person);
    }
} 