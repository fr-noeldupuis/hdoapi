package fr.noeldupuis.hdoapi.enrollment.dto;

import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UpdateEnrollmentRequest {
    
    private Enrollment.EnrollmentStatus status;
    private String notes;
} 