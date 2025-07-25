package fr.noeldupuis.hdoapi.pilgrimage.controller;

import fr.noeldupuis.hdoapi.common.dto.PagedResponse;
import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageResource;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.service.PilgrimageService;
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
public class PilgrimageController {
    
    private static final String BASE_PATH = "/api/pilgrimages";
    
    private final PilgrimageService pilgrimageService;
    
    @GetMapping
    public ResponseEntity<PagedResponse<PilgrimageResource>> getAllPilgrimages(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
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
    public ResponseEntity<PilgrimageResource> getPilgrimageById(@PathVariable Long id) {
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
    public ResponseEntity<PilgrimageResource> createPilgrimage(@RequestBody CreatePilgrimageRequest request) {
        PilgrimageDto createdPilgrimage = pilgrimageService.createPilgrimage(request);
        PilgrimageResource pilgrimageResource = new PilgrimageResource(createdPilgrimage);
        pilgrimageResource.add(Link.of(BASE_PATH + "/" + createdPilgrimage.getId(), "self"));
        pilgrimageResource.add(Link.of(BASE_PATH, "collection"));
        return ResponseEntity.status(HttpStatus.CREATED).body(pilgrimageResource);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PilgrimageResource> updatePilgrimage(@PathVariable Long id, @RequestBody UpdatePilgrimageRequest request) {
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
    public ResponseEntity<Void> deletePilgrimage(@PathVariable Long id) {
        boolean deleted = pilgrimageService.deletePilgrimage(id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
} 