package fr.noeldupuis.hdoapi.pilgrimage.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "pilgrimages", itemRelation = "pilgrimage")
public class PilgrimageResource extends RepresentationModel<PilgrimageResource> {
    
    private Long id;
    private String name;
    private LocalDate startDate;
    private LocalDate endDate;
    
    public PilgrimageResource(PilgrimageDto pilgrimageDto) {
        this.id = pilgrimageDto.getId();
        this.name = pilgrimageDto.getName();
        this.startDate = pilgrimageDto.getStartDate();
        this.endDate = pilgrimageDto.getEndDate();
    }
} 