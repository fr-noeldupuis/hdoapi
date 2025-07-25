package fr.noeldupuis.hdoapi.pilgrimage.service;

import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;

import java.util.List;
import java.util.Optional;

public interface PilgrimageService {
    
    List<PilgrimageDto> getAllPilgrimages();
    
    Optional<PilgrimageDto> getPilgrimageById(Long id);
    
    PilgrimageDto createPilgrimage(CreatePilgrimageRequest request);
    
    Optional<PilgrimageDto> updatePilgrimage(Long id, UpdatePilgrimageRequest request);
    
    boolean deletePilgrimage(Long id);
} 