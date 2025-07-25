package fr.noeldupuis.hdoapi.pilgrimage.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Request to create a new pilgrimage")
public class CreatePilgrimageRequest {
    
    @Schema(description = "Name of the pilgrimage", example = "Summer Pilgrimage 2025", required = true)
    private String name;
    
    @Schema(description = "Start date of the pilgrimage", example = "2025-07-01", required = true)
    private LocalDate startDate;
    
    @Schema(description = "End date of the pilgrimage", example = "2025-07-15", required = true)
    private LocalDate endDate;
} 