package fr.noeldupuis.hdoapi.persons.service;

import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PartialUpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PatchPersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {
    
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;
    
    @Override
    public Page<PersonDto> getAllPersons(Pageable pageable) {
        return personRepository.findAll(pageable)
                .map(this::convertToDto);
    }
    
    @Override
    public Optional<PersonDto> getPersonById(Long id) {
        return personRepository.findById(id)
                .map(this::convertToDto);
    }
    
    @Override
    public PersonDto createPerson(CreatePersonRequest request) {
        Person person = new Person();
        person.setFirstName(request.getFirstName());
        person.setLastName(request.getLastName());
        person.setBirthDate(request.getBirthDate());
        
        Person savedPerson = personRepository.save(person);
        return convertToDto(savedPerson);
    }
    
    @Override
    public Optional<PersonDto> updatePerson(Long id, UpdatePersonRequest request) {
        return personRepository.findById(id)
                .map(person -> {
                    person.setFirstName(request.getFirstName());
                    person.setLastName(request.getLastName());
                    person.setBirthDate(request.getBirthDate());
                    
                    Person updatedPerson = personRepository.save(person);
                    return convertToDto(updatedPerson);
                });
    }
    
    @Override
    public boolean deletePerson(Long id) {
        if (personRepository.existsById(id)) {
            personRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    // PATCH method implementations
    @Override
    @Transactional
    public PersonDto partialUpdatePerson(Long id, PartialUpdatePersonRequest request) {
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
        
        Person updatedPerson = personRepository.save(person);
        return convertToDto(updatedPerson);
    }
    
    @Override
    @Transactional
    public PersonDto patchPerson(Long id, List<PatchPersonRequest.Operation> operations) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        
        for (PatchPersonRequest.Operation operation : operations) {
            applyOperation(person, operation);
        }
        
        Person updatedPerson = personRepository.save(person);
        return convertToDto(updatedPerson);
    }
    
    @Override
    @Transactional
    public PersonDto mergePatchPerson(Long id, JsonNode patch) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        
        // Convert person to JsonNode
        JsonNode personNode = objectMapper.valueToTree(person);
        
        // Apply the patch
        JsonNode patchedNode = applyJsonMergePatch(personNode, patch);
        
        // Convert back to Person
        Person patchedPerson = objectMapper.convertValue(patchedNode, Person.class);
        patchedPerson.setId(id); // Ensure ID is preserved
        
        Person updatedPerson = personRepository.save(patchedPerson);
        return convertToDto(updatedPerson);
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
                    person.setBirthDate(java.time.LocalDate.parse((String) value));
                } else if (value instanceof java.time.LocalDate) {
                    person.setBirthDate((java.time.LocalDate) value);
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
    
    private JsonNode applyJsonMergePatch(JsonNode target, JsonNode patch) {
        if (!target.isObject() || !patch.isObject()) {
            return patch;
        }
        
        com.fasterxml.jackson.databind.node.ObjectNode result = 
            (com.fasterxml.jackson.databind.node.ObjectNode) target.deepCopy();
        
        patch.fieldNames().forEachRemaining(fieldName -> {
            JsonNode patchValue = patch.get(fieldName);
            if (patchValue.isNull()) {
                result.remove(fieldName);
            } else {
                result.set(fieldName, patchValue);
            }
        });
        
        return result;
    }
    
    private PersonDto convertToDto(Person person) {
        return new PersonDto(
                person.getId(),
                person.getFirstName(),
                person.getLastName(),
                person.getBirthDate()
        );
    }
} 