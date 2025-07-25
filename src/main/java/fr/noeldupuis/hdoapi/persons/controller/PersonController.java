package fr.noeldupuis.hdoapi.persons.controller;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.PersonResource;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.service.PersonService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
public class PersonController {
    
    private static final String BASE_PATH = "/api/persons";
    
    private final PersonService personService;
    
    @GetMapping
    public ResponseEntity<PagedResponse<PersonResource>> getAllPersons(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PersonDto> personPage = personService.getAllPersons(pageable);
        
        List<PersonResource> personResources = personPage.getContent().stream()
                .map(PersonResource::new)
                .collect(Collectors.toList());
        
        // Add HATEOAS links to each person resource
        personResources.forEach(personResource -> {
            personResource.add(Link.of(BASE_PATH + "/" + personResource.getId(), "self"));
            personResource.add(Link.of(BASE_PATH, "collection"));
        });
        
        PagedResponse<PersonResource> response = new PagedResponse<>(
                personResources, 
                personPage, 
                BASE_PATH
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PersonResource> getPersonById(@PathVariable Long id) {
        return personService.getPersonById(id)
                .map(personDto -> {
                    PersonResource personResource = new PersonResource(personDto);
                    personResource.add(Link.of(BASE_PATH + "/" + id, "self"));
                    personResource.add(Link.of(BASE_PATH, "collection"));
                    return ResponseEntity.ok(personResource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PersonResource> createPerson(@RequestBody CreatePersonRequest request) {
        PersonDto createdPerson = personService.createPerson(request);
        PersonResource personResource = new PersonResource(createdPerson);
        personResource.add(Link.of(BASE_PATH + "/" + createdPerson.getId(), "self"));
        personResource.add(Link.of(BASE_PATH, "collection"));
        return ResponseEntity.status(HttpStatus.CREATED).body(personResource);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PersonResource> updatePerson(@PathVariable Long id, @RequestBody UpdatePersonRequest request) {
        return personService.updatePerson(id, request)
                .map(personDto -> {
                    PersonResource personResource = new PersonResource(personDto);
                    personResource.add(Link.of(BASE_PATH + "/" + id, "self"));
                    personResource.add(Link.of(BASE_PATH, "collection"));
                    return ResponseEntity.ok(personResource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePerson(@PathVariable Long id) {
        boolean deleted = personService.deletePerson(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 