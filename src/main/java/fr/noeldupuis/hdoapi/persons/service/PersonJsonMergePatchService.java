package fr.noeldupuis.hdoapi.persons.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import fr.noeldupuis.hdoapi.persons.entity.Person;
import fr.noeldupuis.hdoapi.persons.repository.PersonRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PersonJsonMergePatchService {
    
    private final PersonRepository personRepository;
    private final ObjectMapper objectMapper;
    
    @Transactional
    public Person patchPerson(Long id, JsonNode patch) {
        Person person = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Person not found with id: " + id));
        
        // Convert person to JsonNode
        JsonNode personNode = objectMapper.valueToTree(person);
        
        // Apply the patch
        JsonNode patchedNode = applyJsonMergePatch(personNode, patch);
        
        // Convert back to Person
        Person patchedPerson = objectMapper.convertValue(patchedNode, Person.class);
        patchedPerson.setId(id); // Ensure ID is preserved
        
        return personRepository.save(patchedPerson);
    }
    
    private JsonNode applyJsonMergePatch(JsonNode target, JsonNode patch) {
        if (!target.isObject() || !patch.isObject()) {
            return patch;
        }
        
        ObjectNode result = (ObjectNode) target.deepCopy();
        
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
} 