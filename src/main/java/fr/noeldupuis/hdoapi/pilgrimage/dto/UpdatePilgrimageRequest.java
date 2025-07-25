package fr.noeldupuis.hdoapi.pilgrimage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to update an existing pilgrimage")
public class UpdatePilgrimageRequest {
    
    @Schema(description = "Name of the pilgrimage", example = "Summer Pilgrimage 2025")
    private String name;
    
    @Schema(description = "Start date of the pilgrimage", example = "2025-07-01")
    private LocalDate startDate;
    
    @Schema(description = "End date of the pilgrimage", example = "2025-07-15")
    private LocalDate endDate;
} 