package fr.noeldupuis.hdoapi.persons.controller;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.persons.dto.CreatePersonRequest;
import fr.noeldupuis.hdoapi.persons.dto.PersonDto;
import fr.noeldupuis.hdoapi.persons.dto.PersonResource;
import fr.noeldupuis.hdoapi.persons.dto.UpdatePersonRequest;
import fr.noeldupuis.hdoapi.persons.service.PersonService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/persons")
@RequiredArgsConstructor
@Tag(name = "Person Management", description = "APIs for managing pilgrimage participants")
public class PersonController {
    
    private static final String BASE_PATH = "/api/persons";
    
    private final PersonService personService;
    
    @GetMapping
    @Operation(
        summary = "Get all persons",
        description = "Retrieve a paginated list of all pilgrimage participants with sorting options"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved persons",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PagedResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "firstName": "John",
                          "lastName": "Doe",
                          "birthDate": "1990-01-01",
                          "_links": {
                            "self": {"href": "/api/persons/1"},
                            "collection": {"href": "/api/persons"}
                          }
                        }
                      ],
                      "pageMetadata": {
                        "page": 0,
                        "size": 10,
                        "totalElements": 1,
                        "totalPages": 1,
                        "first": true,
                        "last": true,
                        "hasNext": false,
                        "hasPrevious": false
                      },
                      "_links": {
                        "self": {"href": "/api/persons?page=0&size=10"}
                      }
                    }
                    """)))
    })
    public ResponseEntity<PagedResponse<PersonResource>> getAllPersons(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "firstName")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "asc")
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
    @Operation(
        summary = "Get person by ID",
        description = "Retrieve a specific pilgrimage participant by their ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved person",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PersonResource.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "firstName": "John",
                      "lastName": "Doe",
                      "birthDate": "1990-01-01",
                      "_links": {
                        "self": {"href": "/api/persons/1"},
                        "collection": {"href": "/api/persons"}
                      }
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<PersonResource> getPersonById(
            @Parameter(description = "Person ID", example = "1")
            @PathVariable Long id) {
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
    @Operation(
        summary = "Create a new person",
        description = "Create a new pilgrimage participant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Person created successfully",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PersonResource.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "firstName": "John",
                      "lastName": "Doe",
                      "birthDate": "1990-01-01",
                      "_links": {
                        "self": {"href": "/api/persons/1"},
                        "collection": {"href": "/api/persons"}
                      }
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PersonResource> createPerson(
            @Parameter(description = "Person data", required = true)
            @RequestBody CreatePersonRequest request) {
        PersonDto createdPerson = personService.createPerson(request);
        PersonResource personResource = new PersonResource(createdPerson);
        personResource.add(Link.of(BASE_PATH + "/" + createdPerson.getId(), "self"));
        personResource.add(Link.of(BASE_PATH, "collection"));
        return ResponseEntity.status(HttpStatus.CREATED).body(personResource);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a person",
        description = "Update an existing pilgrimage participant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Person updated successfully",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PersonResource.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "firstName": "John",
                      "lastName": "Doe",
                      "birthDate": "1990-01-01",
                      "_links": {
                        "self": {"href": "/api/persons/1"},
                        "collection": {"href": "/api/persons"}
                      }
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "Person not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PersonResource> updatePerson(
            @Parameter(description = "Person ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated person data", required = true)
            @RequestBody UpdatePersonRequest request) {
        return personService.updatePerson(id, request)
                .map(personDto -> {
                    PersonResource personResource = new PersonResource(personDto);
                    personResource.add(Link.of(BASE_PATH + "/" + id, "self"));
                    personResource.add(Link.of(BASE_PATH, "collection"));
                    return ResponseEntity.ok(personResource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    // JSON Merge Patch (RFC 7386) - Standard partial update
    @PatchMapping("/{id}")
    @Operation(
        summary = "Partially update a person using JSON Merge Patch (RFC 7386)",
        description = "Update specific fields of an existing pilgrimage participant using JSON Merge Patch format"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Person partially updated successfully",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PersonResource.class))),
        @ApiResponse(responseCode = "404", description = "Person not found"),
        @ApiResponse(responseCode = "400", description = "Invalid patch data")
    })
    public ResponseEntity<PersonResource> patchPerson(
            @Parameter(description = "Person ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "JSON Merge Patch - include only fields to update", required = true)
            @RequestBody JsonNode patch) {
        
        PersonDto updatedPerson = personService.patchPerson(id, patch);
        PersonResource personResource = new PersonResource(updatedPerson);
        
        // Add HATEOAS links
        personResource.add(Link.of(BASE_PATH + "/" + id, "self"));
        personResource.add(Link.of(BASE_PATH, "collection"));
        
        return ResponseEntity.ok(personResource);
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a person",
        description = "Delete a pilgrimage participant"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Person deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Person not found")
    })
    public ResponseEntity<Void> deletePerson(
            @Parameter(description = "Person ID", example = "1")
            @PathVariable Long id) {
        boolean deleted = personService.deletePerson(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 