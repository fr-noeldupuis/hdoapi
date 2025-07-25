package fr.noeldupuis.hdoapi.pilgrimage.controller;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageResource;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.service.PilgrimageService;
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

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/pilgrimages")
@RequiredArgsConstructor
@Tag(name = "Pilgrimage Management", description = "APIs for managing pilgrimage events")
public class PilgrimageController {
    
    private static final String BASE_PATH = "/api/pilgrimages";
    
    private final PilgrimageService pilgrimageService;
    
    @GetMapping
    @Operation(
        summary = "Get all pilgrimages",
        description = "Retrieve a paginated list of all pilgrimage events with sorting options"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pilgrimages",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PagedResponse.class),
                examples = @ExampleObject(value = """
                    {
                      "content": [
                        {
                          "id": 1,
                          "name": "Summer Pilgrimage 2025",
                          "startDate": "2025-07-01",
                          "endDate": "2025-07-15",
                          "_links": {
                            "self": {"href": "/api/pilgrimages/1"},
                            "collection": {"href": "/api/pilgrimages"}
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
                        "self": {"href": "/api/pilgrimages?page=0&size=10"}
                      }
                    }
                    """)))
    })
    public ResponseEntity<PagedResponse<PilgrimageResource>> getAllPilgrimages(
            @Parameter(description = "Page number (0-based)", example = "0")
            @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Number of items per page", example = "10")
            @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Field to sort by", example = "name")
            @RequestParam(defaultValue = "id") String sortBy,
            @Parameter(description = "Sort direction (asc or desc)", example = "asc")
            @RequestParam(defaultValue = "asc") String sortDir) {
        
        Sort sort = sortDir.equalsIgnoreCase("desc") ? 
            Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);
        
        Page<PilgrimageDto> pilgrimagePage = pilgrimageService.getAllPilgrimages(pageable);
        
        List<PilgrimageResource> pilgrimageResources = pilgrimagePage.getContent().stream()
                .map(PilgrimageResource::new)
                .collect(Collectors.toList());
        
        // Add HATEOAS links to each pilgrimage resource
        pilgrimageResources.forEach(pilgrimageResource -> {
            pilgrimageResource.add(Link.of(BASE_PATH + "/" + pilgrimageResource.getId(), "self"));
            pilgrimageResource.add(Link.of(BASE_PATH, "collection"));
        });
        
        PagedResponse<PilgrimageResource> response = new PagedResponse<>(
                pilgrimageResources, 
                pilgrimagePage, 
                BASE_PATH
        );
        
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    @Operation(
        summary = "Get pilgrimage by ID",
        description = "Retrieve a specific pilgrimage event by its ID"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved pilgrimage",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PilgrimageResource.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "name": "Summer Pilgrimage 2025",
                      "startDate": "2025-07-01",
                      "endDate": "2025-07-15",
                      "_links": {
                        "self": {"href": "/api/pilgrimages/1"},
                        "collection": {"href": "/api/pilgrimages"}
                      }
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "Pilgrimage not found")
    })
    public ResponseEntity<PilgrimageResource> getPilgrimageById(
            @Parameter(description = "Pilgrimage ID", example = "1")
            @PathVariable Long id) {
        return pilgrimageService.getPilgrimageById(id)
                .map(pilgrimageDto -> {
                    PilgrimageResource pilgrimageResource = new PilgrimageResource(pilgrimageDto);
                    pilgrimageResource.add(Link.of(BASE_PATH + "/" + id, "self"));
                    pilgrimageResource.add(Link.of(BASE_PATH, "collection"));
                    return ResponseEntity.ok(pilgrimageResource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    @Operation(
        summary = "Create a new pilgrimage",
        description = "Create a new pilgrimage event"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pilgrimage created successfully",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PilgrimageResource.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "name": "Summer Pilgrimage 2025",
                      "startDate": "2025-07-01",
                      "endDate": "2025-07-15",
                      "_links": {
                        "self": {"href": "/api/pilgrimages/1"},
                        "collection": {"href": "/api/pilgrimages"}
                      }
                    }
                    """))),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PilgrimageResource> createPilgrimage(
            @Parameter(description = "Pilgrimage data", required = true)
            @RequestBody CreatePilgrimageRequest request) {
        PilgrimageDto createdPilgrimage = pilgrimageService.createPilgrimage(request);
        PilgrimageResource pilgrimageResource = new PilgrimageResource(createdPilgrimage);
        pilgrimageResource.add(Link.of(BASE_PATH + "/" + createdPilgrimage.getId(), "self"));
        pilgrimageResource.add(Link.of(BASE_PATH, "collection"));
        return ResponseEntity.status(HttpStatus.CREATED).body(pilgrimageResource);
    }
    
    @PutMapping("/{id}")
    @Operation(
        summary = "Update a pilgrimage",
        description = "Update an existing pilgrimage event"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pilgrimage updated successfully",
            content = @Content(mediaType = "application/hal+json",
                schema = @Schema(implementation = PilgrimageResource.class),
                examples = @ExampleObject(value = """
                    {
                      "id": 1,
                      "name": "Summer Pilgrimage 2025",
                      "startDate": "2025-07-01",
                      "endDate": "2025-07-15",
                      "_links": {
                        "self": {"href": "/api/pilgrimages/1"},
                        "collection": {"href": "/api/pilgrimages"}
                      }
                    }
                    """))),
        @ApiResponse(responseCode = "404", description = "Pilgrimage not found"),
        @ApiResponse(responseCode = "400", description = "Invalid request data")
    })
    public ResponseEntity<PilgrimageResource> updatePilgrimage(
            @Parameter(description = "Pilgrimage ID", example = "1")
            @PathVariable Long id,
            @Parameter(description = "Updated pilgrimage data", required = true)
            @RequestBody UpdatePilgrimageRequest request) {
        return pilgrimageService.updatePilgrimage(id, request)
                .map(pilgrimageDto -> {
                    PilgrimageResource pilgrimageResource = new PilgrimageResource(pilgrimageDto);
                    pilgrimageResource.add(Link.of(BASE_PATH + "/" + id, "self"));
                    pilgrimageResource.add(Link.of(BASE_PATH, "collection"));
                    return ResponseEntity.ok(pilgrimageResource);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    
    @DeleteMapping("/{id}")
    @Operation(
        summary = "Delete a pilgrimage",
        description = "Delete a pilgrimage event"
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pilgrimage deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Pilgrimage not found")
    })
    public ResponseEntity<Void> deletePilgrimage(
            @Parameter(description = "Pilgrimage ID", example = "1")
            @PathVariable Long id) {
        boolean deleted = pilgrimageService.deletePilgrimage(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 