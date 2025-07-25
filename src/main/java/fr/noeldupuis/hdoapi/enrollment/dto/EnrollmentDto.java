package fr.noeldupuis.hdoapi.enrollment.dto;

import fr.noeldupuis.hdoapi.enrollment.entity.Enrollment;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentDto {
    
    private Long id;
    private Long personId;
    private String personName;
    private Long pilgrimageId;
    private String pilgrimageName;
    private LocalDateTime enrollmentDate;
    private Enrollment.EnrollmentStatus status;
    private String notes;
} 