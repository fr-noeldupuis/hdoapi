package fr.noeldupuis.hdoapi.pilgrimage.controller;

import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.service.PilgrimageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/pilgrimages")
@RequiredArgsConstructor
public class PilgrimageController {
    
    private final PilgrimageService pilgrimageService;
    
    @GetMapping
    public ResponseEntity<List<PilgrimageDto>> getAllPilgrimages() {
        List<PilgrimageDto> pilgrimages = pilgrimageService.getAllPilgrimages();
        return ResponseEntity.ok(pilgrimages);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<PilgrimageDto> getPilgrimageById(@PathVariable Long id) {
        return pilgrimageService.getPilgrimageById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<PilgrimageDto> createPilgrimage(@RequestBody CreatePilgrimageRequest request) {
        PilgrimageDto createdPilgrimage = pilgrimageService.createPilgrimage(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPilgrimage);
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<PilgrimageDto> updatePilgrimage(@PathVariable Long id, @RequestBody UpdatePilgrimageRequest request) {
        return pilgrimageService.updatePilgrimage(id, request)
                .map(ResponseEntity::ok)
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