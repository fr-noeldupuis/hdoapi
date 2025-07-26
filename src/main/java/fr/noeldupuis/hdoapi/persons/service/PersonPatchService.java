package fr.noeldupuis.hdoapi.persons.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.noeldupuis.hdoapi.persons.dto.PatchPersonRequest;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonPatchService {
    
    private final PersonRepository personRepository;
    
    @Transactional
    public Person patchPerson(Long id, List<PatchPersonRequest.Operation> operations) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        
        for (PatchPersonRequest.Operation operation : operations) {
            applyOperation(person, operation);
        }
        
        return personRepository.save(person);
    }
    
    private void applyOperation(Person person, PatchPersonRequest.Operation operation) {
        switch (operation.getOp().toLowerCase()) {
            case "replace":
                applyReplace(person, operation);
                break;
            case "add":
                applyAdd(person, operation);
                break;
            case "remove":
                applyRemove(person, operation);
                break;
            default:
                throw new RuntimeException("Unsupported operation: " + operation.getOp());
        }
    }
    
    private void applyReplace(Person person, PatchPersonRequest.Operation operation) {
        String path = operation.getPath();
        Object value = operation.getValue();
        
        switch (path) {
            case "/firstName":
                person.setFirstName((String) value);
                break;
            case "/lastName":
                person.setLastName((String) value);
                break;
            case "/birthDate":
                if (value instanceof String) {
                    person.setBirthDate(LocalDate.parse((String) value));
                } else if (value instanceof LocalDate) {
                    person.setBirthDate((LocalDate) value);
                }
                break;
            default:
                throw new RuntimeException("Invalid path for replace: " + path);
        }
    }
    
    private void applyAdd(Person person, PatchPersonRequest.Operation operation) {
        // For simple entities, add is same as replace
        applyReplace(person, operation);
    }
    
    private void applyRemove(Person person, PatchPersonRequest.Operation operation) {
        String path = operation.getPath();
        
        switch (path) {
            case "/firstName":
                person.setFirstName(null);
                break;
            case "/lastName":
                person.setLastName(null);
                break;
            case "/birthDate":
                person.setBirthDate(null);
                break;
            default:
                throw new RuntimeException("Invalid path for remove: " + path);
        }
    }
} 