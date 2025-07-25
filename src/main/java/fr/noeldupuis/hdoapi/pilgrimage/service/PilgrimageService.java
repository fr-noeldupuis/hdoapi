package fr.noeldupuis.hdoapi.pilgrimage.service;

import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

public interface PilgrimageService {
    
    Page<PilgrimageDto> getAllPilgrimages(Pageable pageable);
    
    Optional<PilgrimageDto> getPilgrimageById(Long id);
    
    PilgrimageDto createPilgrimage(CreatePilgrimageRequest request);
    
    Optional<PilgrimageDto> updatePilgrimage(Long id, UpdatePilgrimageRequest request);
    
    boolean deletePilgrimage(Long id);
} 