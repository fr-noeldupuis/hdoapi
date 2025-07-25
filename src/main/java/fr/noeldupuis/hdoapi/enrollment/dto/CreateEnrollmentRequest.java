package fr.noeldupuis.hdoapi.enrollment.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateEnrollmentRequest {
    
    @NotNull(message = "Person ID is required")
    private Long personId;
    
    @NotNull(message = "Pilgrimage ID is required")
    private Long pilgrimageId;
    
    private String notes;
} 