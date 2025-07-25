package fr.noeldupuis.hdoapi.pilgrimage.service;

import fr.noeldupuis.hdoapi.pilgrimage.dto.CreatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.dto.PilgrimageDto;
import fr.noeldupuis.hdoapi.pilgrimage.dto.UpdatePilgrimageRequest;
import fr.noeldupuis.hdoapi.pilgrimage.entity.Pilgrimage;
import fr.noeldupuis.hdoapi.pilgrimage.repository.PilgrimageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PilgrimageServiceImpl implements PilgrimageService {
    
    private final PilgrimageRepository pilgrimageRepository;
    
    @Override
    public List<PilgrimageDto> getAllPilgrimages() {
        return pilgrimageRepository.findAll()
                .stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }
    
    @Override
    public Optional<PilgrimageDto> getPilgrimageById(Long id) {
        return pilgrimageRepository.findById(id)
                .map(this::convertToDto);
    }
    
    @Override
    public PilgrimageDto createPilgrimage(CreatePilgrimageRequest request) {
        Pilgrimage pilgrimage = new Pilgrimage();
        pilgrimage.setName(request.getName());
        pilgrimage.setStartDate(request.getStartDate());
        pilgrimage.setEndDate(request.getEndDate());
        
        Pilgrimage savedPilgrimage = pilgrimageRepository.save(pilgrimage);
        return convertToDto(savedPilgrimage);
    }
    
    @Override
    public Optional<PilgrimageDto> updatePilgrimage(Long id, UpdatePilgrimageRequest request) {
        return pilgrimageRepository.findById(id)
                .map(pilgrimage -> {
                    pilgrimage.setName(request.getName());
                    pilgrimage.setStartDate(request.getStartDate());
                    pilgrimage.setEndDate(request.getEndDate());
                    
                    Pilgrimage updatedPilgrimage = pilgrimageRepository.save(pilgrimage);
                    return convertToDto(updatedPilgrimage);
                });
    }
    
    @Override
    public boolean deletePilgrimage(Long id) {
        if (pilgrimageRepository.existsById(id)) {
            pilgrimageRepository.deleteById(id);
            return true;
        }
        return false;
    }
    
    private PilgrimageDto convertToDto(Pilgrimage pilgrimage) {
        return new PilgrimageDto(
                pilgrimage.getId(),
                pilgrimage.getName(),
                pilgrimage.getStartDate(),
                pilgrimage.getEndDate()
        );
    }
} 