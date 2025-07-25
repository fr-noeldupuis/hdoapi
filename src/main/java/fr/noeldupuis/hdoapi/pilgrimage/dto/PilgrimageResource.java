package fr.noeldupuis.hdoapi.pilgrimage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
@Relation(collectionRelation = "pilgrimages", itemRelation = "pilgrimage")
@Schema(description = "Pilgrimage resource with HATEOAS links")
public class PilgrimageResource extends RepresentationModel<PilgrimageResource> {
    
    @Schema(description = "Unique identifier of the pilgrimage", example = "1")
    private Long id;
    
    @Schema(description = "Name of the pilgrimage", example = "Summer Pilgrimage 2025")
    private String name;
    
    @Schema(description = "Start date of the pilgrimage", example = "2025-07-01")
    private LocalDate startDate;
    
    @Schema(description = "End date of the pilgrimage", example = "2025-07-15")
    private LocalDate endDate;
    
    public PilgrimageResource(PilgrimageDto pilgrimageDto) {
        this.id = pilgrimageDto.getId();
        this.name = pilgrimageDto.getName();
        this.startDate = pilgrimageDto.getStartDate();
        this.endDate = pilgrimageDto.getEndDate();
    }
} 